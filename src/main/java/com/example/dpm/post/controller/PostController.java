package com.example.dpm.post.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.post.dto.PageRequestDto;
import com.example.dpm.post.dto.PageResponseDto;
import com.example.dpm.post.dto.PostDto;
import com.example.dpm.post.model.ImgEntity;
import com.example.dpm.post.service.ImgService;
import com.example.dpm.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@CrossOrigin("*")
public class PostController {
	private final PostService postService;

	
    private final ImgService imgService;


	private final String FOLDER_PATH = "c:\\images\\"; // 로컬 저장 경로

	// 사진 업로드 완료 버튼에 적용 시킬 api
	// 사진 url string으로 변환하는 controller
	@PostMapping("/img")
	public int uploadImage(@RequestPart(value = "image") MultipartFile image) {
        try {
            ImgEntity imgEntity = imgService.uploadImage(image);
            return imgEntity.getImgId();
        } catch (IOException e) {
            return -1;
        }
    }

	// 게시물 생성
	@PostMapping("/")
	public ResponseEntity<Integer> createPost(@Validated @RequestBody PostDto postDto) {
		Integer postId = postService.create(postDto);
		return ResponseEntity.ok(postId); // 생성된 게시물 ID 반환
	}

	// 게시물 조회
	@GetMapping("/{postId}")
	public ResponseEntity<PostDto> getPost(@PathVariable("postId") int postId) {
		PostDto postDto = postService.get(postId);
		return ResponseEntity.ok(postDto); // 게시물 데이터 반환
	}

	// 게시물 수정
	@PutMapping("/{postId}")
	public ResponseEntity<Void> modifyPost(@PathVariable("postId") int postId, @RequestBody PostDto postDto) {
		// 수정하려는 게시글의 ID를 DTO에 설정
		postDto.setPostId(postId);
		// 게시물 수정
		postService.modify(postDto);
		// 204 No Content 반환 (수정 성공)
		return ResponseEntity.noContent().build();
	}

	// 게시글 좋아요 기능
	@PostMapping("/{postId}/like")
	public ResponseEntity<String> toggleLike(@PathVariable("postId") Integer postId) {
		postService.toggleLike(postId);
		return new ResponseEntity<>("Like status updated.", HttpStatus.OK);
	}

	// 페이지네이션된 게시물 리스트 조회 for test
	@GetMapping("/list")
	public ResponseEntity<PageResponseDto<PostDto>> getList(@ModelAttribute PageRequestDto pageRequestDto) {
		PageResponseDto<PostDto> response = postService.getList(pageRequestDto);
		return ResponseEntity.ok(response);
	}

	// 제목으로 게시물 검색
	@GetMapping("/list/search")
	public ResponseEntity<PageResponseDto<PostDto>> searchPostsByTitle(
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			@ModelAttribute PageRequestDto pageRequestDto) {
		PageResponseDto<PostDto> response = postService.searchPostsByTitle(keyword, pageRequestDto);
		return ResponseEntity.ok(response);
	}

	// sort 조건에 따라 게시글 조회
	@GetMapping("/list/{sort}")
	public ResponseEntity<PageResponseDto<PostDto>> getAllPostsBySort(@ModelAttribute PageRequestDto pageRequestDto,
			@PathVariable("sort") String sort) {
		PageResponseDto<PostDto> response;

		switch (sort) {
		case "latest":
			response = postService.getAllPostsOrderByDateLatest(pageRequestDto); // 최신순
			break;
		case "earliest":
			response = postService.getAllPostsOrderByDateEarliest(pageRequestDto); // 오래된순
			break;
		case "likes":
			response = postService.getAllPostsOrderByLikes(pageRequestDto); // 좋아요 많은 순
			break;
		default:
			return ResponseEntity.badRequest().build(); // 잘못된 정렬 기준
		}

		return ResponseEntity.ok(response);
	}

	// tag 검색 후 페이지네이션 게시글 조회
	@GetMapping("/list/search/tag/{tagName}")
	public ResponseEntity<PageResponseDto<PostDto>> searchPostsByTag(@PathVariable("tagName") String tagName,
			@ModelAttribute PageRequestDto pageRequestDto) {
		PageResponseDto<PostDto> response = postService.searchPostsByTag(tagName, pageRequestDto);
		return ResponseEntity.ok(response);
	}

	// 특정 회원의 게시글 조회
	@GetMapping("/list/mypost/{memberId}") // URL 경로에서 memberId를 받음
	public ResponseEntity<PageResponseDto<PostDto>> getAllMyPosts(@PathVariable("memberId") Long memberId,
			@ModelAttribute PageRequestDto pageRequestDto) {
		PageResponseDto<PostDto> myPosts = postService.getAllMyPosts(memberId, pageRequestDto); // 특정 회원의 게시글 조회
		System.out.println("#####[PostController] myposts: " + myPosts.getDtoList());
		return ResponseEntity.ok(myPosts); // 게시글 리스트 반환
	}

	// 게시글 삭제
	@DeleteMapping("/{postId}") // URL 경로에서 postId를 받음
	public ResponseEntity<Void> deletePost(@PathVariable("postId") Integer postId) {
		postService.remove(postId); // 게시글 삭제 서비스 호출
		return ResponseEntity.noContent().build(); // 204 No Content 반환
	}
}
