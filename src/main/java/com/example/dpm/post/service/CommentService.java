package com.example.dpm.post.service;

import com.example.dpm.post.dto.CommentDto;
import com.example.dpm.post.dto.PageRequestDto;
import com.example.dpm.post.dto.PageResponseDto;

public interface CommentService {
	public CommentDto get(Integer commentId); // 댓글 조회
	
	public Integer create(CommentDto commentDto); // 댓글 생성
	
	public void remove(Integer postId, Integer commentId); // 댓글 삭제
	
	PageResponseDto<CommentDto> getCommentsByPostId(Integer postId, PageRequestDto pageRequestDto);

	
}
