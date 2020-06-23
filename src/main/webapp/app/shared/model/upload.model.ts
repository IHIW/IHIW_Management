import { Moment } from 'moment';
import { IIhiwUser } from 'app/shared/model/ihiw-user.model';
import { IValidation } from 'app/shared/model/validation.model';
import { IProject } from 'app/shared/model/project.model';

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
  validations?: IValidation[];
  enabled?: boolean;
  createdBy?: IIhiwUser;
  rawDownload?: string;
  convertedDownload?: string;
  valid?: boolean;
  project?: IProject;
}

export class Upload implements IUpload {
  constructor(
    public id?: number,
    public type?: FileType,
    public createdAt?: Moment,
    public modifiedAt?: Moment,
    public fileName?: string,
    public valid?: boolean,
    public validationFeedback?: string,
    public enabled?: boolean,
    public createdBy?: IIhiwUser,
    public rawDownload?: string,
    public convertedDownload?: string,
    public project?: IProject
  ) {
    this.valid = this.valid || false;
    this.enabled = this.enabled || false;
  }
}
