import { HttpParams } from '@angular/common/http';

export interface BuildParamsOptions {
  /**
   * If true, include boolean false (e.g. hasWiFi=false).
   * Default: false (omit false booleans)
   */
  includeFalseBooleans?: boolean;

  /**
   * If true, include empty arrays as key= (rarely useful).
   * Default: false
   */
  includeEmptyArrays?: boolean;
}

export function buildParams<T extends Record<string, any>>(
  input?: T,
  options: BuildParamsOptions = {}
): HttpParams {
  let params = new HttpParams();

  if (!input) {
    return params;
  }

  const includeFalse = options.includeFalseBooleans ?? false;
  const includeEmptyArrays = options.includeEmptyArrays ?? false;

  for (const [key, value] of Object.entries(input)) {
    // skip null/undefined/empty string
    if (value === null || value === undefined || value === '') {
      continue;
    }

    // booleans: include only true by default
    if (typeof value === 'boolean') {
      if (value === true) {
        params = params.set(key, 'true');
      } else if (includeFalse) {
        params = params.set(key, 'false');
      }
      continue;
    }

    // arrays: append each item
    if (Array.isArray(value)) {
      if (value.length === 0 && !includeEmptyArrays) {
        continue;
      }
      for (const item of value) {
        if (item === null || item === undefined || item === '') {
          continue;
        }
        params = params.append(key, String(item));
      }
      continue;
    }

    // numbers: optionally skip NaN
    if (typeof value === 'number') {
      if (Number.isNaN(value)) {
        continue;
      }
      params = params.set(key, String(value));
      continue;
    }

    // default
    params = params.set(key, String(value));
  }

  return params;
}