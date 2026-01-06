import { HttpParams } from "@angular/common/http";

export function buildParams<T extends object>(input?: T): HttpParams{
    let params = new HttpParams();

    // if we have no params input 
    if(!input){
        return params;
    }

    // Ex:  page?: number; (Key and Value) ; convert object to array in Ts

    for( const [key, value] of Object.entries(input)){
        if(value == null || value === ''){
            continue; // skip this step 
        }

        // it have condition : can be single value or Array 
        if(Array.isArray(input)){
            for(const item of value){
                params = params.append(key, String(item));
            }
        } else{
            params = params.set(key, String(value));
        }
    }
    return params;
}


/*

We want to convert our own pagram to http param 

export interface RoomListParams {
    page?: number;
    size?: number;
    sort?: string;
    roomType?: string;
    properType?: string;
    price?: number;
}

*/