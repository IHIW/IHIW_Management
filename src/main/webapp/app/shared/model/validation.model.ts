import { IUpload } from 'app/shared/model/upload.model';

export interface IValidation {
  id?: number;
  upload?: IUpload;
  valid?: boolean;
  validationFeedback?: string;
  validator?: string;
}

export class Validation implements IValidation {
  constructor(
    public id?: number,
    public upload?: IUpload,
    public valid?: boolean,
    public validationFeedback?: string,
    public validator?: string
  ) {}
}
