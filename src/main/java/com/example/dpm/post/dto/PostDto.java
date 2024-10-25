package com.example.dpm.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
	private int postId;
	private Long memberId; 
	private String title;
	private String content;
	private LocalDate postDate;
	private int imgId;
	private List<TagDto> tags;
	private List<CommentDto> comments;
	private String emoji;
	private int totalLikeHeart; 
	private boolean myLikeHeart;
	private String memberName;
}
