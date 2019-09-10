import { IUser } from 'app/core/user/user.model';
import { IUpload } from 'app/shared/model/upload.model';
import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';

export interface IIhiwUser {
  id?: number;
  phone?: string;
  user?: IUser;
  uploads?: IUpload[];
  lab?: IIhiwLab;
}

export class IhiwUser implements IIhiwUser {
  constructor(public id?: number, public phone?: string, public user?: IUser, public uploads?: IUpload[], public lab?: IIhiwLab) {}
}
