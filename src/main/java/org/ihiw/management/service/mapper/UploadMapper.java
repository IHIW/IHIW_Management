package org.ihiw.management.service.mapper;

import org.ihiw.management.domain.Project;
import org.ihiw.management.domain.Upload;
import org.ihiw.management.service.dto.ProjectDTO;
import org.ihiw.management.service.dto.UploadDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UploadMapper {
    public List<UploadDTO> UploadsToUploadDTOs(List<Upload> uploads) {
        return uploads.stream()
            .filter(Objects::nonNull)
            .map(this::UploadToUploadDTO)
            .collect(Collectors.toList());
    }



    public UploadDTO UploadToUploadDTO(Upload upload) {
        return new UploadDTO(upload);
    }



    public Upload UploadDTOTOUpload(UploadDTO uploadDTO) {
        if (uploadDTO == null) {
            return null;
        } else {
            Upload upload = new Upload();
            upload.setId(uploadDTO.getId());
            upload.setCreatedBy(uploadDTO.getCreatedBy());
            upload.setConvertedDownload(uploadDTO.getConvertedDownload());
            upload.setRawDownload(uploadDTO.getRawDownload());
            upload.setCreatedAt(uploadDTO.getCreatedAt());
            upload.setFileName(uploadDTO.getFileName());
            upload.setModifiedAt(uploadDTO.getModifiedAt());
            upload.setValid(uploadDTO.isValid());
            upload.setEnabled(uploadDTO.isEnabled());
            upload.setType(uploadDTO.getType());

            return upload;
        }
    }

}
