import { Moment } from 'moment';
import { IProject } from 'app/shared/model/project.model';
import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';

export interface IProjectIhiwLab {
  status?: string;
  lab?: IIhiwLab;
  id?: number;
}

export class ProjectIhiwLab implements IProjectIhiwLab {
  constructor(public status?: string, public labs?: IIhiwLab, public id?: number) {}
}
