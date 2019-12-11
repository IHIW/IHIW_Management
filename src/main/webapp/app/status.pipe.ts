import { Pipe, PipeTransform } from '@angular/core';
import { IProjectIhiwLab } from './shared/model/project-ihiw-lab.model';

@Pipe({
  name: 'status',
  pure: false
})
export class StatusPipe implements PipeTransform {
  transform(input: IProjectIhiwLab[], status: string) {
    const output: IProjectIhiwLab[] = [];
    for (let i = 0; i < input.length; i++) {
      if (input[i].status !== status) {
        output.push(input[i]);
      }
    }
    return output;
  }
}
