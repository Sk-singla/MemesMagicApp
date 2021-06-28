package com.samarth.memesmagic.ui.Screens.home.create

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samarth.data.models.request.PostRequest
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.models.MemeTemplate
import com.samarth.memesmagic.data.remote.models.PostType
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Constants
import com.samarth.memesmagic.util.Constants.BUCKET_OBJECT_URL_PREFIX
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.getJwtToken
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ja.burhanrashid52.photoeditor.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL
import java.util.*
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class CreateViewModel  @Inject constructor(
    val memeRepo: MemeRepo
):ViewModel() {

    // FOR CREATE SCREEN ( TEMPLATE SELECTION)
    val templatesList = mutableStateOf(mutableListOf<MemeTemplate>())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var curPage = 1
    private var pagesLeft = (0..Constants.MAXIMUM_MEME_MAKER_PAGE_NUMBER).toList()

    init {
        getMemeTemplates()
    }

    fun getMemeTemplates() = viewModelScope.launch{
        isLoading.value = true

        pagesLeft = pagesLeft - curPage
        when(val result =  memeRepo.getMemeTemplates(curPage)){

            is Resource.Success -> {
                endReached.value = pagesLeft.isEmpty()
                templatesList.value.addAll(result.data!!)

                if(!endReached.value)
                    curPage = pagesLeft.random()

                loadError.value = ""
                isLoading.value = false
            }
            is Resource.Error -> {
                loadError.value = result.message ?: "Error!"
                isLoading.value = false
            }
            else -> {
                loadError.value = ""
                isLoading.value = false
            }

        }
    }



    // FOR EDITING SCREEN ->
    lateinit var photoEditor:PhotoEditor

    val brushMode = mutableStateOf(false)
    val notToolMode = mutableStateOf(true)
    val brushSize = mutableStateOf(0f)
    val curBrushColor = mutableStateOf(Color.WHITE)
    val opacity = mutableStateOf(100)
    val curEraserSize = mutableStateOf(0f)
    val eraserMode = mutableStateOf(false)
    val textMode = mutableStateOf(false)
    val selectedTextView = mutableStateOf<View?>(null)
    val curText = mutableStateOf("")
    val curTextColor = mutableStateOf(Color.WHITE)
    val caption = mutableStateOf("")

    val coloursList = listOf(Color.BLACK,Color.BLUE,Color.RED,Color.CYAN,Color.GREEN,Color.DKGRAY,Color.MAGENTA,Color.WHITE,Color.YELLOW)



    val toolsList = listOf(
            EditTool("Brush", R.drawable.ic_baseline_brush_24) {
                   initBrush()
            },
            EditTool("Eraser", R.drawable.ic_eraser) {
                     initEraserMode()
            },
            EditTool("Text", R.drawable.ic_baseline_text_fields_24) {
                        initTextMode()
            },
            EditTool("Emoji", R.drawable.ic_outline_emoji) {

            }
        )


    fun initPhotoEditor(
        context: Context,
        photoEditorView: PhotoEditorView,
        imageUrl:String
    ){

       Single.fromCallable {
            return@fromCallable BitmapFactory.decodeStream(
                URL(imageUrl).openConnection().getInputStream()
            )
       }
           .subscribeOn(Schedulers.io())
           .subscribe { bmp ->
               photoEditorView.source.setImageBitmap(bmp)
               photoEditListeners()
           }
        photoEditor = PhotoEditor
            .Builder(context,photoEditorView)
            .setPinchTextScalable(true)
            .build()
        photoEditor.setBrushDrawingMode(false)
    }


    fun setBrushSize(brushSizeVar:Float) {
        photoEditor.brushSize = brushSizeVar
        brushSize.value = brushSizeVar
        setBrushColor(curBrushColor.value)
    }

    fun setBrushColor(@ColorInt color:Int){
        photoEditor.brushColor = color
        curBrushColor.value = color
    }

    fun setOpacity(op:Int){
        photoEditor.setOpacity(op)
        opacity.value = op
    }

    fun initBrush(){
        photoEditor.setBrushDrawingMode(true)
        brushMode.value = true
        notToolMode.value = false
        brushSize.value = photoEditor.brushSize
        photoEditor.brushColor = Color.WHITE
        curBrushColor.value = Color.WHITE
    }

    fun endBrushMode(){
        photoEditor.setBrushDrawingMode(false)
        brushMode.value = false
        notToolMode.value = true
    }

    fun initEraserMode(){
        curEraserSize.value = photoEditor.eraserSize
        photoEditor.brushEraser()
        eraserMode.value = true
        notToolMode.value = false
    }

    fun setEraserSize(size:Float){
        photoEditor.setBrushEraserSize(size)
        curEraserSize.value = size
        photoEditor.brushEraser()
    }

    fun endEraserMode(){
        photoEditor.setBrushDrawingMode(false)
        eraserMode.value = false
        notToolMode.value = true
    }


    fun initTextMode(){

        textMode.value = true
        notToolMode.value = false

    }


    fun photoEditListeners(){
        photoEditor.setOnPhotoEditorListener(object :OnPhotoEditorListener{
            override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
                initTextMode()
                curText.value = text ?: ""
                curTextColor.value = colorCode
                selectedTextView.value = rootView
            }

            override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {

            }

            override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {

            }

            override fun onStartViewChangeListener(viewType: ViewType?) {

            }

            override fun onStopViewChangeListener(viewType: ViewType?) {

            }
        })
    }

    fun setTextColor(clr:Int){
        curTextColor.value = clr
    }

    fun addText() {
        photoEditor.addText(curText.value,curTextColor.value)
        endTextMode()
    }

    fun editText(){
        if(selectedTextView.value != null) {
            Log.d("MyLog","Curtext value ->  ${curText.value}")
            photoEditor.editText(selectedTextView.value!!, curText.value, curTextColor.value)
        }
        endTextMode()
    }

    fun endTextMode(){
        textMode.value = false
        notToolMode.value = true
        curText.value = ""
        curTextColor.value = Color.WHITE
        selectedTextView.value = null
    }




    fun saveImageToInternalStorage(context: Context,bmp:Bitmap,filename:String = UUID.randomUUID().toString()):Boolean{
        return try{
            context.openFileOutput(filename, MODE_PRIVATE).use { stream->
                if(!bmp.compress(Bitmap.CompressFormat.JPEG,95,stream)){
                    throw IOException("Couldn't Save Bitmap!")
                }
                true
            }
        }catch (e:Exception){
            e.printStackTrace()
            false
        }
    }

    fun saveAsBitmapInInternalStorage(context: Context,fileName:String = UUID.randomUUID().toString(),onSuccess: () -> Unit, onFail: (String) -> Unit){

        photoEditor.saveAsBitmap(object :OnSaveBitmap{
            override fun onBitmapReady(saveBitmap: Bitmap?) {
                saveBitmap?.let { bmp->
                    if(saveImageToInternalStorage(context,bmp,fileName)){
                        onSuccess()
                    } else {
                        onFail("Can't save to Internal Storage!!")
                    }
                }
            }

            override fun onFailure(e: java.lang.Exception?) {
                onFail(e?.message ?: "Failed to create Bitmap!!")
            }
        })

    }


    fun uploadPost(context: Context,onFail:(String)->Unit,onSuccess:()->Unit) {
        val fileName = "${UUID.randomUUID().toString()}.jpg"

        saveAsBitmapInInternalStorage(
            context,
            fileName=fileName,
            onSuccess = {
                viewModelScope.launch {
                    memeRepo.uploadFileOnAwsS3(
                        context = context,
                        fileName = fileName,
                        onSuccess = { awsKey ->
                            viewModelScope.launch {
                                val result = memeRepo.uploadPost(
                                    token = getJwtToken(context)!!,
                                    postRequest = PostRequest(
                                        PostType.IMAGE,
                                        System.currentTimeMillis(),
                                        tags = listOf(),
                                        mediaLink = BUCKET_OBJECT_URL_PREFIX + awsKey,
                                        description = caption.value
                                    )
                                )

                                when(result){
                                    is Resource.Success -> {
                                        onSuccess()
                                    }
                                    else -> {
                                        onFail(result.message ?: "Some Problem Occurred!!")
                                    }
                                }
                            }
                        },
                        onFail = onFail
                    )
                }
            },
            onFail = onFail
        )
    }










}