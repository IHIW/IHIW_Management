import { Moment } from 'moment';
import { IProject } from 'app/shared/model/project.model';
import { IIhiwUser } from 'app/shared/model/ihiw-user.model';

export interface IIhiwLab {
  id?: number;
  labCode?: string;
  title?: string;
  firstName?: string;
  lastName?: string;
  director?: string;
  department?: string;
  institution?: string;
  address1?: string;
  address2?: string;
  sAddress1?: string;
  sAddress?: string;
  city?: string;
  state?: string;
  zip?: string;
  country?: string;
  phone?: string;
  fax?: string;
  email?: string;
  url?: string;
  oldLabCode?: string;
  sName?: string;
  sPhone?: string;
  sEmail?: string;
  dName?: string;
  dEmail?: string;
  dPhone?: string;
  createdAt?: Moment;
  projects?: IProject[];
  ihiwUsers?: IIhiwUser[];
}

export class IhiwLab implements IIhiwLab {
  constructor(
    public id?: number,
    public labCode?: string,
    public title?: string,
    public firstName?: string,
    public lastName?: string,
    public director?: string,
    public department?: string,
    public institution?: string,
    public address1?: string,
    public address2?: string,
    public sAddress1?: string,
    public sAddress?: string,
    public city?: string,
    public state?: string,
    public zip?: string,
    public country?: string,
    public phone?: string,
    public fax?: string,
    public email?: string,
    public url?: string,
    public oldLabCode?: string,
    public sName?: string,
    public sPhone?: string,
    public sEmail?: string,
    public dName?: string,
    public dEmail?: string,
    public dPhone?: string,
    public createdAt?: Moment,
    public projects?: IProject[],
    public ihiwUsers?: IIhiwUser[]
  ) {}
}
