import { Pipe, PipeTransform } from '@angular/core';
import { IProjectIhiwLab } from './shared/model/project-ihiw-lab.model';

@Pipe({
  name: 'status',
  pure: false
})
export class StatusPipe implements PipeTransform {
  transform(input: IProjectIhiwLab[], statusString: string) {
    const output: IProjectIhiwLab[] = [];
    const statusList = statusString.split(';');
    for (let i = 0; i < input.length; i++) {
      let doPush = true;
      for (let j = 0; j < statusList.length; j++) {
        if (input[i].status === statusList[j]) {
          doPush = false;
        }
      }
      if (doPush) {
        output.push(input[i]);
      }
    }
    return output;
  }
}
