import {Headers} from "angular2/http";

export function headers() {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    return headers;
}

export function indexOfId(array: IdInterface[], id: string): number {
    const length = array.length;
    for (let i = 0; i < length; i++) {
        if (array[i].id === id) {
            // console.log(i);
            return i;
        }
    }
    // console.log(-1);
    return -1;
}

export interface IdInterface {
    id: string;
}
