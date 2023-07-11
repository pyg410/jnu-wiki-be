package com.timcooki.jnuwiki.domain.docsRequest.service;

import com.timcooki.jnuwiki.domain.docs.DTO.DocsCreateDTO;
import com.timcooki.jnuwiki.domain.docs.DTO.DocsUpdateInfoDTO;
import com.timcooki.jnuwiki.domain.docsRequest.dto.request.CreatedRequestWriteDTO;
import com.timcooki.jnuwiki.domain.docsRequest.dto.request.ModifiedRequestWriteDTO;
import com.timcooki.jnuwiki.domain.docsRequest.entity.DocsRequest;
import com.timcooki.jnuwiki.domain.member.DTO.response.admin.CreatedFindAllReqDTO;
import com.timcooki.jnuwiki.domain.member.DTO.response.admin.CreatedFindByIdReqDTO;
import com.timcooki.jnuwiki.domain.member.DTO.response.admin.ModifiedFindByIdReqDTO;
import com.timcooki.jnuwiki.domain.member.DTO.response.admin.ModifiedFindAllReqDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class DocsRequestService {

    public DocsRequest createModifiedRequest(ModifiedRequestWriteDTO modifiedRequestWriteDto) {
    }

    public DocsRequest createCreatedRequest(CreatedRequestWriteDTO createdRequestDto) {
    }
    
    public Page<ModifiedFindAllReqDTO> getModifiedRequestList(Pageable pageable) {
    }

    public Page<CreatedFindAllReqDTO> getCreatedRequestList(Pageable pageable) {
    }

    public ModifiedFindByIdReqDTO getOneModifiedRequest(String docsRequestId) {
    }

    public CreatedFindByIdReqDTO getOneCreatedRequest(String docsRequestId) {
    }

    public DocsCreateDTO createDocsFromRequest(String docsRequestId) {
    }

    public DocsUpdateInfoDTO updateDocsFromRequest(String docsRequestId) {
    }

    public void rejectRequest(String docsRequestId) {
    }

    public boolean hasRequest(String docsRequestId) {
    }
}
