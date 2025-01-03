package com.example.dpm.post.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.member.repository.MemberRepository;
import com.example.dpm.post.dto.CommentDto;
import com.example.dpm.post.dto.ImgDto;
import com.example.dpm.post.dto.PageRequestDto;
import com.example.dpm.post.dto.PageResponseDto;
import com.example.dpm.post.dto.PostDto;
import com.example.dpm.post.dto.TagDto;
import com.example.dpm.post.model.CommentEntity;
import com.example.dpm.post.model.ImgEntity;
import com.example.dpm.post.model.PostEntity;
import com.example.dpm.post.model.TagEntity;
import com.example.dpm.post.repository.ImgRepository;
import com.example.dpm.post.repository.PostRepository;
import com.example.dpm.post.repository.TagRepository;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private ImgRepository imgRepository;

	@Autowired
	private ImgService imgService;

	private final String FOLDER_PATH = "c:\\images\\"; // 로컬 저장 경로

	@Override
	public PostDto get(int postId) {
		Optional<PostEntity> result = postRepository.findById(postId);
		PostEntity post = result.orElseThrow();
		return toDto(post);
	}

	@Override
	public void toggleLike(int postId) {
		Optional<PostEntity> result = postRepository.findById(postId);
		PostEntity post = result.orElseThrow();

		post.toggleLikeHeart();

		postRepository.save(post);
	}

	@Transactional
	@Override
	public int create(PostDto dto) {
		Optional<MemberEntity> member = memberRepository.findById(dto.getMemberId());
		MemberEntity foundMember = member.orElseThrow();

		// DTO에서 태그 DTO 리스트를 가져와서 TagEntity들을 찾거나 새로 생성
		List<TagEntity> tags = new ArrayList<>();
		for (TagDto tagDto : dto.getTags()) {
			Optional<TagEntity> existingTag = tagRepository.findByTagName(tagDto.getName());
			TagEntity tagEntity;
			if (existingTag.isPresent()) {
				// 기존 태그를 사용
				tagEntity = existingTag.get();
			} else {
				// 새로운 태그 생성
				tagEntity = new TagEntity();
				tagEntity.setTagName(tagDto.getName());
				tagEntity = tagRepository.save(tagEntity); // 새로 생성된 태그 저장
			}
			tags.add(tagEntity);
		}
		
		// 이미지 ID를 통해 ImgEntity 조회
		ImgEntity imgEntity = imgRepository.findById(dto.getImgId())
				.orElseThrow(() -> new RuntimeException("Image not found"));
		
		// PostEntity 생성
		PostEntity postEntity = toEntity(dto, foundMember, tags, null, imgEntity);

		PostEntity result = postRepository.save(postEntity);
		return result.getPostId(); // 몇번째인지 return
	}

	@Transactional
	@Override
	public void modify(PostDto dto) {
		PostEntity postEntity = postRepository.findById(dto.getPostId())
				.orElseThrow(() -> new RuntimeException("Post not found"));

		// 제목, 내용,이모지 수정
		postEntity.setTitle(dto.getTitle());
		postEntity.setContent(dto.getContent());
		postEntity.setEmoji(dto.getEmoji());

		// 이미지 수정
		if (dto.getImgId() != 0) {
			ImgEntity imgEntity = imgRepository.findById(dto.getImgId())
					.orElseThrow(() -> new RuntimeException("Image not found"));
			imgEntity.setFilePath(imgEntity.getFilePath());

			// 수정된 이미지 설정
			postEntity.setImg(imgEntity);
		}

		// 태그 업데이트
		List<TagEntity> tags = new ArrayList<>();
		for (TagDto tagDto : dto.getTags()) {
			Optional<TagEntity> existingTag = tagRepository.findByTagName(tagDto.getName());
			TagEntity tagEntity;
			if (existingTag.isPresent()) {
				tagEntity = existingTag.get();
			} else {
				tagEntity = new TagEntity();
				tagEntity.setTagName(tagDto.getName());
				tagRepository.save(tagEntity);
			}
			tags.add(tagEntity);
		}

		// 게시물의 태그 업데이트
		postEntity.setTags(tags);

		// 수정된 게시글을 저장
		postRepository.save(postEntity);
	}

	@Override
	public void remove(int postId) {
		// 게시글이 존재하는지 확인
		Optional<PostEntity> postEntity = postRepository.findById(postId);
		if (postEntity.isPresent()) {
			postRepository.deleteById(postId); // 게시글 삭제
		} else {
			throw new RuntimeException("게시글이 존재하지 않습니다."); // 게시글이 없을 경우 예외 처리
		}
	}

	@Override
	public List<PostDto> getAllPosts() {
		return postRepository.findAll().stream().map(this::toDto) // PostEntity를 PostDto로 변환하는 메서드
				.collect(Collectors.toList());
	}

	@Override
	public PageResponseDto<PostDto> getList(PageRequestDto pageRequestDto) {
		// 페이지네이션을 위한 계산
		int page = pageRequestDto.getPage() - 1; // JPA는 페이지 번호가 0부터 시작
		int size = pageRequestDto.getSize();
		int offset = page * size;

		// 페이지네이션에 맞춰 게시물 목록 조회
		List<PostEntity> postEntities = postRepository.findPostsWithPagination(offset, size);

		// 게시물 총 개수 조회
		long totalPosts = postRepository.count();

		// PostEntity를 PostDto로 변환
		List<PostDto> postDtos = postEntities.stream().map(this::toDto).collect(Collectors.toList());

		// PageResponseDto로 변환하여 반환
		return PageResponseDto.<PostDto>withAll().dtoList(postDtos).pageRequestDto(pageRequestDto).total(totalPosts)
				.build();
	}

	@Override
	public PageResponseDto<PostDto> searchPostsByTitle(String keyword, PageRequestDto pageRequestDto) {
		PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());
		Page<PostEntity> postEntities = postRepository.searchByTitle(keyword, pageRequest);

		return new PageResponseDto<>(postEntities.stream().map(this::toDto).collect(Collectors.toList()),
				pageRequestDto, postEntities.getTotalElements());
	}

	@Override
	public PageResponseDto<PostDto> getAllPostsOrderByDateLatest(PageRequestDto pageRequestDto) {
		PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());
		Page<PostEntity> postEntities = postRepository.findAllOrderByPostDateLatest(pageRequest);

		return new PageResponseDto<>(postEntities.stream().map(this::toDto).collect(Collectors.toList()),
				pageRequestDto, postEntities.getTotalElements());
	}

	@Override
	public PageResponseDto<PostDto> getAllPostsOrderByDateEarliest(PageRequestDto pageRequestDto) {
		PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());
		Page<PostEntity> postEntities = postRepository.findAllOrderByPostDateEarliest(pageRequest);

		return new PageResponseDto<>(postEntities.stream().map(this::toDto).collect(Collectors.toList()),
				pageRequestDto, postEntities.getTotalElements());
	}

	@Override
	public PageResponseDto<PostDto> getAllPostsOrderByLikes(PageRequestDto pageRequestDto) {
		PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());
		Page<PostEntity> postEntities = postRepository.findAllOrderByLikes(pageRequest); // 좋아요 많은 순으로 조회

		return new PageResponseDto<>(postEntities.stream().map(this::toDto).collect(Collectors.toList()),
				pageRequestDto, postEntities.getTotalElements());
	}

	@Override
	public PageResponseDto<PostDto> searchPostsByTag(String tagName, PageRequestDto pageRequestDto) {
		// 페이지네이션을 위한 계산
		int page = pageRequestDto.getPage() - 1; // JPA는 페이지 번호가 0부터 시작
		int size = pageRequestDto.getSize();

		Page<PostEntity> postEntities = postRepository.findPostsByTagName(tagName, PageRequest.of(page, size));

		// PostEntity를 PostDto로 변환
		List<PostDto> postDtos = postEntities.getContent().stream().map(this::toDto).collect(Collectors.toList());

		// PageResponseDto로 변환하여 반환
		return PageResponseDto.<PostDto>withAll().dtoList(postDtos).pageRequestDto(pageRequestDto)
				.total(postEntities.getTotalElements()).build();
	}

	@Override
	public PageResponseDto<PostDto> getAllMyPosts(Long memberId, PageRequestDto pageRequestDto) {
		// 페이지네이션을 위한 계산
		int page = pageRequestDto.getPage() - 1; // JPA는 페이지 번호가 0부터 시작
		int size = pageRequestDto.getSize();

		Page<PostEntity> postEntities = postRepository.findByMember_MemberId(memberId, PageRequest.of(page, size));
		List<PostDto> postDtos = postEntities.getContent().stream().map(this::toDto).collect(Collectors.toList());
		return PageResponseDto.<PostDto>withAll().dtoList(postDtos).pageRequestDto(pageRequestDto)
				.total(postEntities.getTotalElements()).build();
	}

	// PostDto -> PostEntity 변환
	public PostEntity toEntity(PostDto dto, MemberEntity member, List<TagEntity> tags, List<CommentEntity> comments,
			ImgEntity imgEntity) {

		return PostEntity.builder().postId(dto.getPostId()).member(member).title(dto.getTitle())
				.content(dto.getContent()).postDate(dto.getPostDate()).img(imgEntity).emoji(dto.getEmoji())
				.totalLikeHeart(dto.getTotalLikeHeart()).myLikeHeart(dto.isMyLikeHeart()).tags(tags) // 여러 태그 설정
				.comments(comments).build();
	}

	// PostEntity -> PostDto 변환
	public PostDto toDto(PostEntity entity) {
		List<TagDto> tags = entity.getTags().stream().map(tag -> new TagDto(tag.getTagId(), tag.getTagName()))
				.collect(Collectors.toList());

		List<CommentDto> comments = entity.getComments().stream()
	            .map(comment -> new CommentDto(
	                    comment.getCommentId(),
	                    comment.getMember().getMember_id(),
	                    comment.getPost().getPostId(),
	                    comment.getContent(),
	                    comment.getCommentDate(),
	                    comment.getMember().getNickname()
	            ))
	            .collect(Collectors.toList());

		return PostDto.builder().postId(entity.getPostId()).memberId(entity.getMember().getMember_id())
				.title(entity.getTitle()).content(entity.getContent()).postDate(entity.getPostDate()).imgId(entity.getImg().getImgId())
				.emoji(entity.getEmoji()).totalLikeHeart(entity.getTotalLikeHeart()).myLikeHeart(entity.isMyLikeHeart())
				.tags(tags).comments(comments).memberName(entity.getMember().getNickname()).build();
	}

}
