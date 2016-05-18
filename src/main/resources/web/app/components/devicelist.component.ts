import {Component} from 'angular2/core';
import {Device, DevicesService} from "../services/device.service";
import {searchFilterPipe} from "../pipes/searchFilterPipe";

@Component({
    selector: 'Devices',
    pipes: [
        searchFilterPipe
    ],
    template: `
        <div class="input-field col s6">
          <input #listFilter (keyup)="0" placeholder="Suche" id="first_name" type="text" class="validate">
        </div>
        <ul class="collection">
            <li *ngFor="#device of devices | searchFilter:listFilter.value" class="collection-item avatar">
                <i class="material-icons circle"><i class="material-icons">local_florist</i></i>
                <span class="title">{{device.id}}</span>
                <p>{{device.state}}<br>
                    Second Line
                </p>
                <div class="secondary-content">
                <div *ngIf="device.locationX >= 1000.0">
                    <a class="btn-floating btn waves-effect waves-light" (click)="add(device.id)"  ><i class="material-icons">add</i></a>
                </div>
                <div *ngIf="device.locationX <= 1000.0">
                    <a class="btn-floating btn waves-effect waves-light" (click)="edit(device.id)" ><i class="material-icons">mode_edit</i></a>
                    <a class="btn-floating btn waves-effect waves-light" (click)="delete(device.id)" ><i class="material-icons">delete_forever</i></a>
                </div>

                </div>
            </li>
        </ul>

    `
})

export class DevicesComponent {

    private devices: Array<Device> = [];
    private interval;

    constructor(private _devicesService: DevicesService) {

        this._devicesService.devicesSubject
            .subscribe( devices => {
                    this.devices = devices;
                }
            );
        this._devicesService.load();
        this.interval = setInterval(() => this._devicesService.load(), 5000 );
    }

    add(id: string) {
        this._devicesService.enterEditModeForDevice(id).subscribe(
            res => {
                console.log("Einrichtung erfolgt");
            },
            error => {
                console.log("fehler bei der Einrichtung aufgetretten");
            }
        );
    }

    delete(id: string) {
        this._devicesService.deleteDevice(id).subscribe(
            res => {
                console.log("gelöscht");
            },
            error => {
                console.log("fehler beim löschen aufgetretten");
            }
        );
    }

    edit(id: string) {
        this._devicesService.enterEditModeForDevice(id).subscribe(
            res => {
                console.log("Einrichtung erfolgt");
            },
            error => {
                console.log("fehler bei der Einrichtung aufgetretten");
            }
        );
    }
}
