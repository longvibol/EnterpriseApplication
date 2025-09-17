package com.piseth.java.school.addressservice.domain;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import com.piseth.java.school.addressservice.domain.enumeration.AdminLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("adminAreas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminArea {

	@Id
	private String code;

	private AdminLevel level;
	private String parentCode;
	private String nameKh;
	private String nameEn;
	private List<String> path;

	// audit
	@CreatedDate
	private Instant createAt;
	
	@LastModifiedDate
	private Instant updateAt;

	@Version
	private Long version;

}

/* 
 
   [
  {
    "code": "12",
    "level": "PROVINCE",
    "parentCode": null,
    "path": [
      "12"
    ],
    "nameKh": "ភ្នំពេញ",
    "nameEn": "Phnom Penh",
    "createdAt": "2025-09-07T01:30:04.430Z",
    "updatedAt": "2025-09-07T01:30:04.430Z"
  },
  {
    "code": "12-03",
    "level": "DISTRICT",
    "parentCode": "12",
    "path": [
      "12",
      "12-03"
    ],
    "nameKh": "ខណ្ឌទួលគោក",
    "nameEn": "Toul Kork",
    "createdAt": "2025-09-07T01:30:45.230Z",
    "updatedAt": "2025-09-07T01:30:45.230Z"
  }
]

 
 
 */