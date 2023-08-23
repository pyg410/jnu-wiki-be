package com.timcooki.jnuwiki.domain.docs.service;

import com.timcooki.jnuwiki.domain.docs.DTO.request.ContentEditReqDTO;
import com.timcooki.jnuwiki.domain.docs.DTO.response.ContentEditResDTO;
import com.timcooki.jnuwiki.domain.docs.DTO.response.ListReadResDTO;
import com.timcooki.jnuwiki.domain.docs.DTO.response.ReadResDTO;
import com.timcooki.jnuwiki.domain.docs.DTO.response.SearchReadResDTO;
import com.timcooki.jnuwiki.domain.docs.entity.Docs;
import com.timcooki.jnuwiki.domain.docs.mapper.DocsMapper;
import com.timcooki.jnuwiki.domain.docs.repository.DocsRepository;
import com.timcooki.jnuwiki.domain.member.entity.Member;
import com.timcooki.jnuwiki.domain.member.repository.MemberRepository;
import com.timcooki.jnuwiki.domain.scrap.entity.ScrapId;
import com.timcooki.jnuwiki.domain.scrap.repository.ScrapRepository;
import com.timcooki.jnuwiki.util.errors.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocsService {
    private final DocsRepository docsRepository;
    private final MemberRepository memberRepository;
    private final ScrapRepository scrapRepository;

    public List<ListReadResDTO> getDocsList(String email, Pageable pageable) {
        if (email != null) {
            Member member = memberRepository.findByEmail(email).get();
            return docsRepository.mFindAllByScrapUser(member.getMemberId(), pageable);
        }

        Page<Docs> list = docsRepository.findAll(pageable);

        return list.stream()
                .map(docs -> new ListReadResDTO(
                        docs.getDocsId(),
                        docs.getDocsName(),
                        docs.getDocsCategory().getCategory(),
                        false))
                .collect(Collectors.toList());
    }

    @Transactional
    public ContentEditResDTO updateDocs(Long docsId, ContentEditReqDTO contentEditReqDTO) {
        Docs docs = docsRepository.findById(docsId).orElseThrow(
                () -> new Exception404("존재하지 않는 문서입니다.")
        );
        // TODO - 가입일 15일 체크 추가

        docs.updateContent(contentEditReqDTO.docsContent());

        return new ContentEditResDTO(
                docs.getDocsId(),
                docs.getDocsContent(),
                docs.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
        );
    }

    public ReadResDTO getOneDocs(String email, Long docsId) {
        boolean scrap = false;
        Docs docs = docsRepository.findById(docsId).orElseThrow(
                () -> new Exception404("존재하지 않는 문서입니다.")
        );

        if (email != null) {
            Member member = memberRepository.findByEmail(email).get();
            if (scrapRepository.findById(new ScrapId(member.getMemberId(), docs.getDocsId())).isPresent()) scrap = true;
        }

        return new ReadResDTO(
                docs.getDocsId(),
                docs.getDocsName(),
                docs.getDocsCategory().getCategory(),
                docs.getDocsLocation(),
                docs.getDocsContent(),
                docs.getCreatedBy().getNickName(),
                docs.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                scrap
        );
    }

    public List<SearchReadResDTO> searchLike(String search) {
        List<Docs> docsList = docsRepository.searchLike(search);
        if (docsList != null && !docsList.isEmpty()) {
            DocsMapper mapper = Mappers.getMapper(DocsMapper.class);

            List<SearchReadResDTO> res = docsList.stream().map((docs -> mapper.entityToDTO(docs, docs.getCreatedBy().getNickName(), docs.getDocsCategory().getCategory()))).toList();

            return res;
        } else {
            throw new Exception404("요청결과가 존재하지 않습니다.");
        }

    }
}
