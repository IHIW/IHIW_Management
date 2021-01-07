import { Moment } from 'moment';
import { IIhiwUser } from 'app/shared/model/ihiw-user.model';
import { IValidation } from 'app/shared/model/validation.model';
import { IProject } from 'app/shared/model/project.model';

export const enum FileType {
  HAML = 'HAML',
  HML = 'HML',
  PED = 'PED',
  PROJECT_DATA_MATRIX = 'PROJECT_DATA_MATRIX',
  XLSX = 'XLSX',
  ANTIBODY_CSV = 'ANTIBODY_CSV',
  FASTQ = 'FASTQ',
  OTHER = 'OTHER'
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
  valid?: boolean;
  project?: IProject;
  parentUpload?: IUpload;
  hasChildren?: boolean;
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
    public project?: IProject,
    public upload?: IUpload
  ) {
    this.valid = this.valid || false;
    this.enabled = this.enabled || false;
  }
}
