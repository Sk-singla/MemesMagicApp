package com.samarth.memesmagic.ui.screens.comments

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.samarth.memesmagic.data.remote.request.CommentRequest
import com.samarth.memesmagic.data.remote.response.Comment
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.CommentsUtil
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.getEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    app:Application,
    val memeRepo: MemeRepo
):AndroidViewModel(app) {



    val comment = mutableStateOf("")
    val post = mutableStateOf(CommentsUtil.post)
    val commentsList = mutableStateOf(post.value?.comments ?: mutableListOf<Comment>())

    var userEmail:String? = null
    init {
        viewModelScope.launch {
            userEmail = getEmail(getApplication<Application>().applicationContext)!!
        }
    }

    fun addComment(onSuccess:(Comment)->Unit) = viewModelScope.launch{

        val trimmedComment = comment.value.trim()
        if(trimmedComment.isEmpty()){
            return@launch
        }
        post.value?.let { pt ->
            val result = memeRepo.addComment(
                pt.id,
                CommentRequest(text = comment.value, time = System.currentTimeMillis())
            )


            if (result is Resource.Success) {
//                commentsList.value.add(0,result.data!!)
                onSuccess(result.data!!)
            }
        }
    }


    fun isCommentLiked(comment: Comment):Boolean{
        return comment.likedBy.map { it.email }.contains(userEmail)
    }

    fun likeUnlikeToggle(comment:Comment,onSuccess: () -> Unit) = viewModelScope.launch{
        post.value?.let { pt ->

            val isLiked = isCommentLiked(comment)
            val result = if(!isLiked) {
                memeRepo.likeComment(
                    pt.id,
                    comment.id
                )
            }else {
                memeRepo.dislikeComment(
                    pt.id,
                    comment.id
                )
            }

            if(result is Resource.Success){
                if(isLiked){
                    comment.likedBy.remove(result.data!!)
                } else {
                    comment.likedBy.add(result.data!!)
                }
                onSuccess()
            }
        }

    }


}