import { Moment } from 'moment';
import { IIhiwUser } from 'app/shared/model/ihiw-user.model';
import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';

export const enum ProjectComponentEnum {
  AntigenicityImmunogenicity = 'Antigenicity & Immunogenicity',
  Immunogenetics = 'Immunogenetics',
  Bioinformatics = 'Bioinformatics'
}

export interface IProject {
  id?: number;
  name?: string;
  component?: ProjectComponentEnum;
  description?: string;
  activated?: boolean;
  createdAt?: Moment;
  modifiedAt?: Moment;
  createdBy?: IIhiwUser;
  modifiedBy?: IIhiwUser;
  labs?: IIhiwLab[];
  leaders?: IIhiwUser[];
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public name?: string,
    public component?: ProjectComponentEnum,
    public description?: string,
    public activated?: boolean,
    public createdAt?: Moment,
    public modifiedAt?: Moment,
    public createdBy?: IIhiwUser,
    public modifiedBy?: IIhiwUser,
    public labs?: IIhiwLab[],
    public leaders?: IIhiwUser[]
  ) {}
}
