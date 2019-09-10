import { Moment } from 'moment';
import { IIhiwUser } from 'app/shared/model/ihiw-user.model';

export const enum FileType {
  HAML = 'HAML',
  HML = 'HML'
}

export interface IUpload {
  id?: number;
  type?: FileType;
  createdAt?: Moment;
  modifiedAt?: Moment;
  fileName?: string;
  valid?: boolean;
  enabled?: boolean;
  createdBy?: IIhiwUser;
}

export class Upload implements IUpload {
  constructor(
    public id?: number,
    public type?: FileType,
    public createdAt?: Moment,
    public modifiedAt?: Moment,
    public fileName?: string,
    public valid?: boolean,
    public enabled?: boolean,
    public createdBy?: IIhiwUser
  ) {
    this.valid = this.valid || false;
    this.enabled = this.enabled || false;
  }
}
