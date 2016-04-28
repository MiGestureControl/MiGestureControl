import {Injectable} from "angular2/core";
import {Http, Response} from "angular2/http";
import {headers, indexOfId} from "./comon";
import {Subject} from "rxjs/Subject";

@Injectable()
export class DevicesService {

    devicesSubject: Subject<Array<Device>> = new Subject<Array<Device>>();
    deviceEditSubject: Subject<Device> = new Subject<Device>();

    private apiBaseUrl: string = 'api/v1/devices';
    private _devices: Array<Device> = [];
    
    constructor(private _http: Http) {}
    
    public load() {
        return this._http.get(this.apiBaseUrl, {headers: headers()})
            .map((res: Response) => res.json())
            .subscribe(
                (devices: Device[]) => {
                    this.setDevices(devices);
                },
                error => {
                    console.log(error);
                }
            );
    }

    enterEditModeForDevice(id: string): any {
        return this._http.get(this.apiBaseUrl + "/" + id, {headers: headers()})
            .map((res: Response) => res.json());
    }

    private setDevices(devices: Device[]) {
        this._devices = devices;
        this.devicesSubject.next(this._devices);
    }
}

export class Device {
    id: string;
    state: string;
}
