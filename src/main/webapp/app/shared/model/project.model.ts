import { Moment } from 'moment';
import { IIhiwUser } from 'app/shared/model/ihiw-user.model';
import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';

export const enum Component {
  AntigenicityImmunogenicity = 'Antigenicity&Immunogenicity',
  Immunogenetics = 'Immunogenetics',
  Bioinformatics = 'Bioinformatics'
}

export interface IProject {
  id?: number;
  name?: string;
  component?: Component;
  description?: string;
  activated?: boolean;
  createdAt?: Moment;
  modifiedAt?: Moment;
  createdBy?: IIhiwUser;
  modifiedBy?: IIhiwUser;
  labs?: IIhiwLab[];
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public name?: string,
    public component?: Component,
    public description?: string,
    public activated?: boolean,
    public createdAt?: Moment,
    public modifiedAt?: Moment,
    public createdBy?: IIhiwUser,
    public modifiedBy?: IIhiwUser,
    public labs?: IIhiwLab[]
  ) {}
}
