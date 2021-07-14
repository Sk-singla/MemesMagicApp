package com.samarth.memesmagic.ui.screens.home.create

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.Color
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.samarth.data.models.request.PostRequest
import com.samarth.memesmagic.R
import com.samarth.memesmagic.data.remote.models.MemeTemplate
import com.samarth.memesmagic.data.remote.models.PostType
import com.samarth.memesmagic.repository.MemeRepo
import com.samarth.memesmagic.util.Constants
import com.samarth.memesmagic.util.Constants.BUCKET_OBJECT_URL_PREFIX
import com.samarth.memesmagic.util.Resource
import com.samarth.memesmagic.util.TokenHandler.getJwtToken
import com.samarth.memesmagic.util.sdk29AndUp
import dagger.hilt.android.lifecycle.HiltViewModel
import ja.burhanrashid52.photoeditor.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.Exception
import java.lang.NumberFormatException


@HiltViewModel
class CreateViewModel  @Inject constructor(
    val memeRepo: MemeRepo
):ViewModel() {

    // FOR CREATE SCREEN ( TEMPLATE SELECTION )
    val templatesList = mutableStateOf(mutableListOf<MemeTemplate>())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var curPage = 0
    private var pagesLeft = (0..Constants.MAXIMUM_MEME_MAKER_PAGE_NUMBER).toList()

    init {
        getMemeTemplates()
    }

    fun getMemeTemplates() = viewModelScope.launch{
        isLoading.value = true
        curPage = pagesLeft.random()
        when(val result =  memeRepo.getMemeTemplates(curPage)){

            is Resource.Success -> {
                endReached.value = pagesLeft.isEmpty()
                templatesList.value.addAll(result.data!!)

                pagesLeft = pagesLeft - curPage

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
    val noToolMode = mutableStateOf(true)
    val brushSize = mutableStateOf(0f)
    val curBrushColor = mutableStateOf(Color.WHITE)
    val opacity = mutableStateOf(100)
    val curEraserSize = mutableStateOf(0f)
    val eraserMode = mutableStateOf(false)
    val textMode = mutableStateOf(false)
    val selectedTextView = mutableStateOf<View?>(null)
    val curText = mutableStateOf("")
    val curTextColor = mutableStateOf(Color.BLACK)
    val caption = mutableStateOf("")
    val emojiMode = mutableStateOf(false)
    var emojiList = mutableStateOf(listOf<String>())

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
            initEmojiMode()
        }
    )


    fun initPhotoEditor(
        context: Context,
        photoEditorView: PhotoEditorView,
        imageUrl:String,
    ){
        Glide.with(context)
            .load(imageUrl)
            .into(photoEditorView.source)

        photoEditor = PhotoEditor
            .Builder(context,photoEditorView)
            .setPinchTextScalable(true)
            .build()

        photoEditor.setBrushDrawingMode(false)
        photoEditListeners()
        emojiList.value = getEmojis(context)
    }

    fun undo() = viewModelScope.launch{
        photoEditor.setBrushDrawingMode(true)
        delay(500)
        photoEditor.undo()
    }

    fun redo() = viewModelScope.launch{
        photoEditor.setBrushDrawingMode(true)
        delay(500)
        photoEditor.redo()
    }


    fun setBrushSize(brushSizeVar:Float)  = viewModelScope.launch{
        photoEditor.brushSize = brushSizeVar
        brushSize.value = brushSizeVar
        delay(500)
        setBrushColor(curBrushColor.value)
    }

    fun setBrushColor(@ColorInt color:Int){
        photoEditor.brushColor = color
        curBrushColor.value = color
    }

    fun setOpacity(op:Int) = viewModelScope.launch{
        photoEditor.setOpacity(op)
        opacity.value = op
        delay(500)
        setBrushColor(curBrushColor.value)
    }

    fun initBrush() = viewModelScope.launch{
        photoEditor.setBrushDrawingMode(true)
        brushMode.value = true
        noToolMode.value = false
        brushSize.value = photoEditor.brushSize
        photoEditor.setBrushDrawingMode(true)

        delay(500)
        photoEditor.brushColor = Color.WHITE
        curBrushColor.value = Color.WHITE
    }

    fun endBrushMode(){
        photoEditor.setBrushDrawingMode(false)
        brushMode.value = false
        noToolMode.value = true
    }

    fun initEraserMode() = viewModelScope.launch {
        curEraserSize.value = photoEditor.eraserSize
        photoEditor.setBrushDrawingMode(false)
        eraserMode.value = true
        noToolMode.value = false
        delay(500)
        photoEditor.brushEraser()
    }

    fun setEraserSize(size:Float) = viewModelScope.launch{
        photoEditor.setBrushEraserSize(size)
        curEraserSize.value = size
        photoEditor.setBrushDrawingMode(false)
        delay(500)
        photoEditor.brushEraser()
    }

    fun endEraserMode(){
        photoEditor.setBrushDrawingMode(false)
        eraserMode.value = false
        noToolMode.value = true
    }


    fun initTextMode(){

        textMode.value = true
        noToolMode.value = false

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
        if(curText.value.isNotEmpty())
            photoEditor.addText(curText.value,curTextColor.value)
        endTextMode()
    }

    fun editText() = viewModelScope.launch{
        photoEditor.setBrushDrawingMode(true)
        delay(500)
        if(selectedTextView.value != null) {
            Log.d("MyLog","Curtext value ->  ${curText.value}")
            photoEditor.editText(selectedTextView.value!!, curText.value, curTextColor.value)
        } else {
            Log.d("MyLog","edit null edit null")
        }
        endTextMode()
    }

    fun endTextMode(){
        textMode.value = false
        noToolMode.value = true
        curText.value = ""
        curTextColor.value = Color.WHITE
        selectedTextView.value = null
    }

    fun initEmojiMode(){
        emojiMode.value = true
        noToolMode.value = false
    }


    fun getEmojis(context: Context): List<String> {
        val emojiList = context.resources.getStringArray(R.array.photo_editor_emoji)
        return emojiList.map { convertEmoji(it) }
    }

    private fun convertEmoji(emoji: String): String {
        return try {
            val convertEmojiToInt = emoji.substring(2).toInt(16)
            String(Character.toChars(convertEmojiToInt))
        } catch (e: NumberFormatException) {
            ""
        }
    }

    fun addEmoji(emoji: String){
        photoEditor.addEmoji(emoji)
        endEmojiMode()
    }

    fun endEmojiMode(){
        emojiMode.value = false
        noToolMode.value = true
    }


    fun saveAsBitmapInExternalStorage(context: Context,fileName:String = UUID.randomUUID().toString(),onSuccess: () -> Unit, onFail: (String) -> Unit){

        val tempPhotoEditor = photoEditor

        photoEditor.saveAsBitmap(object :OnSaveBitmap{
            override fun onBitmapReady(saveBitmap: Bitmap?) {
                saveBitmap?.let { bmp->
                    viewModelScope.launch {
                        if (saveImageToExternalStorage(context, fileName, bmp)) {
                            onSuccess()
                        } else {
                            onFail("Can't save to Internal Storage!!")
                        }
                    }
                }
            }

            override fun onFailure(e: java.lang.Exception?) {
                onFail(e?.message ?: "Failed to create Bitmap!!")
            }
        })

        photoEditor = tempPhotoEditor

    }

    private suspend fun saveImageToExternalStorage(context: Context,displayName:String,bmp:Bitmap):Boolean {
        return withContext(Dispatchers.IO){
            val imageCollection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME,"$displayName.jpg")
                put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
                put(MediaStore.Images.Media.WIDTH,bmp.width)
                put(MediaStore.Images.Media.HEIGHT,bmp.height)
            }
            try {
                context.contentResolver.insert(imageCollection,contentValues)?.also { uri ->
                    context.contentResolver.openOutputStream(uri).use { outputStream->
                        if(!bmp.compress(Bitmap.CompressFormat.JPEG,95,outputStream)){
                            throw IOException("Couldn't save Image")
                        }
                    }
                } ?: throw IOException("Couldn't create Mediastore Entry")
                true
            } catch (e:Exception){
                e.printStackTrace()
                false
            }
        }
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
        isLoading.value = true
        val fileName = "${UUID.randomUUID().toString()}.jpg"

        saveAsBitmapInInternalStorage(
            context,
            fileName=fileName,
            onSuccess = {


                viewModelScope.launch {

                    isLoading.value = true
                    val files = context.filesDir.listFiles()
                    val file = files?.find { it.canRead() && it.isFile && it.name == fileName }
                    memeRepo.uploadFileOnAwsS3(
                        context = context,
                        fileName = fileName,
                        file = file,
                        onSuccess = { awsKey ->
                            viewModelScope.launch {

                                isLoading.value = true

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
                                        caption.value = ""
                                        onSuccess()
                                    }
                                    else -> {
                                        onFail(result.message ?: "Some Problem Occurred!!")
                                    }
                                }
                                isLoading.value = false
                            }
                        },
                        onFail = onFail
                    )
                    isLoading.value = false
                }
            },
            onFail = onFail
        )

        isLoading.value = false
    }










}