
import {Device} from "../services/device.service";
import {PipeTransform} from "angular2/core";
import {Pipe} from "angular2/core";

@Pipe({ name: 'searchFilter' })

export class SearchFilterPipe implements PipeTransform {
    transform(all: Device[], args: string[]) {

        let toFilter = args[0].toLocaleLowerCase();
        return all.filter(device => device.id.toLocaleLowerCase().indexOf(toFilter) !== -1);
    }
}
