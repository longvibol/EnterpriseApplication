package com.piseth.java.school.roomownerservice.service.helper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;

import com.piseth.java.school.roomownerservice.domain.enumeration.GenderPreference;
import com.piseth.java.school.roomownerservice.domain.enumeration.PropertyType;
import com.piseth.java.school.roomownerservice.domain.enumeration.RoomStatus;
import com.piseth.java.school.roomownerservice.domain.enumeration.RoomType;
import com.piseth.java.school.roomownerservice.dto.RoomParsedRow;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class ExcelRoomParserImpl implements ExcelRoomParser {

	private static final Set<String> REQUIRED_HEADERS = Set.of("ownerid", "name", "price", "currencycode", "roomtype",
			"propertytype", "provincecode");

	@Override
	public Flux<RoomParsedRow> parse(final FilePart file) {

		return DataBufferUtils.join(file.content()).flatMapMany(buf -> {
			try {
				final byte[] bytes = toBytes(buf);
				return parseBytes(bytes);
			} finally {
				DataBufferUtils.release(buf);
			}
		});
	}

	private byte[] toBytes(final DataBuffer buf) {

		final byte[] bytes = new byte[buf.readableByteCount()];
		buf.read(bytes);
		return bytes;
	}

	private Flux<RoomParsedRow> parseBytes(final byte[] bytes) {

		return Mono.fromCallable(() -> readRows(bytes))
				.subscribeOn(Schedulers.boundedElastic())
				.flatMapMany(Flux::fromIterable);
	}

	private List<RoomParsedRow> readRows(final byte[] bytes) throws Exception {

		final List<RoomParsedRow> out = new ArrayList<>();

		try (Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(bytes))) {

			// use ss.usermodel.Sheet, no cast from sl.usermodel.Sheet
			final Sheet sheet = wb.getNumberOfSheets() > 0 ? wb.getSheetAt(0) : null;

			if (sheet == null) {
				throw new IllegalArgumentException("Excel file has no sheet!");
			}

			final DataFormatter fmt = new DataFormatter(Locale.ROOT);
			final Row header = sheet.getRow(0);

			if (header == null) {
				throw new IllegalArgumentException("Missing header row");
			}

			final Map<String, Integer> idx = headerIndex(header, fmt);

			if (!idx.keySet().containsAll(REQUIRED_HEADERS)) {
				throw new IllegalArgumentException(
						"Header must include at least: ownerId, name, price, currencyCode, roomType, propertyType, provinceCode");
			}

			final int last = sheet.getLastRowNum();

			for (int r = 1; r <= last; r++) {

				final Row row = sheet.getRow(r);

				if (row == null) {
					continue;
				}

				final String ownerId = cell(row, idx.get("ownerid"), fmt);
				final String name = cell(row, idx.get("name"), fmt);
				final String description = cell(row, idx.get("description"), fmt);

				final Double price = parseDouble(cell(row, idx.get("price"), fmt));
				final String currencyCode = cell(row, idx.get("currencycode"), fmt);

				final Integer floor = parseInteger(cell(row, idx.get("floor"), fmt));
				final Double roomSize = parseDouble(cell(row, idx.get("roomsize"), fmt));

				final RoomType roomType = parseRoomType(cell(row, idx.get("roomtype"), fmt));
				final PropertyType propertyType = parsePropertyType(cell(row, idx.get("propertytype"), fmt));

				final String provinceCode = cell(row, idx.get("provincecode"), fmt);
				final String districtCode = cell(row, idx.get("districtcode"), fmt);
				final String communeCode = cell(row, idx.get("communecode"), fmt);
				final String villageCode = cell(row, idx.get("villagecode"), fmt);

				final String addressLine1 = cell(row, idx.get("addressline1"), fmt);
				final String addressLine2 = cell(row, idx.get("addressline2"), fmt);
				final String postalCode = cell(row, idx.get("postalcode"), fmt);

				final Double latitude = parseDouble(cell(row, idx.get("latitude"), fmt));
				final Double longitude = parseDouble(cell(row, idx.get("longitude"), fmt));

				final Boolean hasFan = parseBoolean(cell(row, idx.get("hasfan"), fmt));
				final Boolean hasAirConditioner = parseBoolean(cell(row, idx.get("hasairconditioner"), fmt));
				final Boolean hasParking = parseBoolean(cell(row, idx.get("hasparking"), fmt));
				final Boolean hasPrivateBathroom = parseBoolean(cell(row, idx.get("hasprivatebathroom"), fmt));
				final Boolean hasBalcony = parseBoolean(cell(row, idx.get("hasbalcony"), fmt));
				final Boolean hasKitchen = parseBoolean(cell(row, idx.get("haskitchen"), fmt));
				final Boolean hasFridge = parseBoolean(cell(row, idx.get("hasfridge"), fmt));
				final Boolean hasWashingMachine = parseBoolean(cell(row, idx.get("haswashingmachine"), fmt));
				final Boolean hasTV = parseBoolean(cell(row, idx.get("hastv"), fmt));
				final Boolean hasWiFi = parseBoolean(cell(row, idx.get("haswifi"), fmt));
				final Boolean hasElevator = parseBoolean(cell(row, idx.get("haselevator"), fmt));

				final Integer maxOccupants = parseInteger(cell(row, idx.get("maxoccupants"), fmt));
				final Boolean isPetFriendly = parseBoolean(cell(row, idx.get("ispetfriendly"), fmt));
				final Boolean isSmokingAllowed = parseBoolean(cell(row, idx.get("issmokingallowed"), fmt));
				final Boolean isSharedRoom = parseBoolean(cell(row, idx.get("issharedroom"), fmt));

				final GenderPreference genderPreference = parseGenderPreference(
						cell(row, idx.get("genderpreference"), fmt));

				final Boolean isUtilityIncluded = parseBoolean(cell(row, idx.get("isutilityincluded"), fmt));
				final Boolean depositRequired = parseBoolean(cell(row, idx.get("depositrequired"), fmt));
				final Double depositAmount = parseDouble(cell(row, idx.get("depositamount"), fmt));
				final Integer minStayMonths = parseInteger(cell(row, idx.get("minstaymonths"), fmt));

				final String contactPhone = cell(row, idx.get("contactphone"), fmt);
				final RoomStatus status = parseRoomStatus(cell(row, idx.get("status"), fmt));

				// Skip completely empty rows
				if (isBlank(ownerId) && isBlank(name) && price == null && isBlank(provinceCode)) {
					continue;
				}

				final RoomParsedRow parsed = new RoomParsedRow(r + 1, ownerId, name, description, price, currencyCode,
						floor, roomSize, roomType, propertyType, provinceCode, districtCode, communeCode, villageCode,
						addressLine1, addressLine2, postalCode, latitude, longitude, hasFan, hasAirConditioner,
						hasParking, hasPrivateBathroom, hasBalcony, hasKitchen, hasFridge, hasWashingMachine, hasTV,
						hasWiFi, hasElevator, maxOccupants, isPetFriendly, isSmokingAllowed, isSharedRoom,
						genderPreference, isUtilityIncluded, depositRequired, depositAmount, minStayMonths,
						contactPhone, status);

				out.add(parsed);
			}
		}

		return out;
	}

	private RoomType parseRoomType(final String raw) {

		if (raw == null) {
			return null;
		}
		final String u = raw.trim().toUpperCase(Locale.ROOT);

		for (final RoomType t : RoomType.values()) {
			if (t.name().equals(u)) {
				return t;
			}
		}

		//throw new IllegalArgumentException("Unknown roomType: " + raw);
		return null;
	}

	private PropertyType parsePropertyType(final String raw) {

		if (raw == null) {
			return null;
		}
		final String u = raw.trim().toUpperCase(Locale.ROOT);

		for (final PropertyType t : PropertyType.values()) {
			if (t.name().equals(u)) {
				return t;
			}
		}

		//throw new IllegalArgumentException("Unknown propertyType: " + raw);
		return null;
	}

	private GenderPreference parseGenderPreference(final String raw) {

		if (raw == null || raw.isBlank()) {
			return null;
		}
		final String u = raw.trim().toUpperCase(Locale.ROOT);

		for (final GenderPreference g : GenderPreference.values()) {
			if (g.name().equals(u)) {
				return g;
			}
		}

		//throw new IllegalArgumentException("Unknown genderPreference: " + raw);
		return null;
	}

	private RoomStatus parseRoomStatus(final String raw) {

		if (raw == null || raw.isBlank()) {
			return null;
		}
		final String u = raw.trim().toUpperCase(Locale.ROOT);

		for (final RoomStatus s : RoomStatus.values()) {
			if (s.name().equals(u)) {
				return s;
			}
		}

		//throw new IllegalArgumentException("Unknown status: " + raw);
		return null;
	}

	private boolean isBlank(final String s) {

		return s == null || s.trim().isEmpty();
	}

	private String cell(final Row row, final Integer col, final DataFormatter fmt) {

		if (row == null || col == null) {
			return null;
		}
		final Cell cell = row.getCell(col);

		if (cell == null) {
			return null;
		}

		final String v = fmt.formatCellValue(cell);

		if (v == null) {
			return null;
		}

		final String t = v.trim();

		if (t.isEmpty()) {
			return null;
		}

		return t;
	}

	private Map<String, Integer> headerIndex(final Row header, final DataFormatter fmt) {

		final Map<String, Integer> idx = new HashMap<>();

		for (int c = 0; c < header.getLastCellNum(); c++) {

			final Cell cell = header.getCell(c);
			final String raw = cell != null ? fmt.formatCellValue(cell) : null;
			final String key = raw != null ? raw.trim().toLowerCase(Locale.ROOT) : "";

			if (!key.isBlank()) {
				idx.put(key, c);
			}
		}

		return idx;
	}

	private Double parseDouble(final String raw) {

		if (raw == null || raw.isBlank()) {
			return null;
		}

		try {
			return Double.valueOf(raw.trim());
		} catch (NumberFormatException ex) {
			//throw new IllegalArgumentException("Invalid number: " + raw);
			return null;
		}
	}

	private Integer parseInteger(final String raw) {

		if (raw == null || raw.isBlank()) {
			return null;
		}

		try {
			return Integer.valueOf(raw.trim());
		} catch (NumberFormatException ex) {
			//throw new IllegalArgumentException("Invalid integer: " + raw);
			return null;
		}
	}

	private Boolean parseBoolean(final String raw) {
		// convert from cell in excel to boolean 

		if (raw == null || raw.isBlank()) {
			return null;
		}

		final String u = raw.trim().toLowerCase(Locale.ROOT);

		return switch (u) {
		case "y", "yes", "true", "1" -> Boolean.TRUE;
		case "n", "no", "false", "0" -> Boolean.FALSE;
		default -> {yield null;}  //throw new IllegalArgumentException("Invalid boolean: " + raw);
		};
	}
}