export interface Page<T> {
    page ?: number;
    size ?: number;
    totalElements ?: number;
    totalPage ?: number;
    content: T[];
}


/*
{
    "page": 0,
    "size": 1,
    "totalElements": 100002,
    "totalPage": 100002,
    "content": []
}
*/