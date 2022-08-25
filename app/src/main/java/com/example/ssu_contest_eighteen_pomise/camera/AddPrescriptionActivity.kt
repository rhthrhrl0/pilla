package com.example.ssu_contest_eighteen_pomise.camera

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ssu_contest_eighteen_pomise.R
import com.example.ssu_contest_eighteen_pomise.camera.add_by_ocr.OcrRegisterActivity
import com.example.ssu_contest_eighteen_pomise.camera.self_add_no_ocr.AddSelfNoOcrActivity
import com.example.ssu_contest_eighteen_pomise.databinding.ActivityAddPrescriptionBinding
import com.example.ssu_contest_eighteen_pomise.extensionfunction.slideNoneAndDownExit
import com.yourssu.design.system.component.Toast.Companion.shortToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class AddPrescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPrescriptionBinding
    private val viewModel: AddPrescriptionViewModel by viewModels()
    private lateinit var dialog: ProgressDialog
    private val filterActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                var currentImageUri = it.data?.data
                try {
                    val curBitmap: Bitmap
                    currentImageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                currentImageUri
                            )
                            curBitmap = bitmap
                        } else {
                            val source =
                                ImageDecoder.createSource(this.contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            curBitmap = bitmap
                        }

                        sendOcrBitmap(curBitmap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (it.resultCode == RESULT_CANCELED) {
                shortToast("사진 선택 취소")
            } else {
                Log.d("ActivityResult", "something wrong")
            }
        }

    private var currentPhotoPath: String? = null
    val requestMultiplePermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map -> //map은 권한의 이름 스트링과 그 권한의 부여 여부 불리언 값이 들어있음.
        if (map[PERMISSION_CAMERA] == true && map[PERMISSION_WRITE_EXTERNAL_STORAGE] == true && map[PERMISSION_READ_EXTERNAL_STORAGE] == true) {
            onStartCamera()
        } else {
            shortToast("권한 얻기 실패...다시 시도해주세요.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_prescription)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        dialog = ProgressDialog(this)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle("이미지 분석중")
        dialog.setMessage("잠시만 기다려주세요")
        dialog.setCancelable(false)


        viewModel.successOcrAndImageData.observe(this@AddPrescriptionActivity, {
            val intent = Intent(this, OcrRegisterActivity::class.java)
            intent.putExtra(OcrRegisterActivity.KEY_OCR_DATA_NAME, it)
            startActivity(intent)
            dialog.dismiss()
        })

        viewModel.finishEvent.observe(this@AddPrescriptionActivity, {
            onBackPressed()
        })

        viewModel.ocrCameraEvent.observe(this@AddPrescriptionActivity, {
            requestPermissions() // 카메라 권한 요청
        })

        viewModel.ocrGalleryEvent.observe(this@AddPrescriptionActivity, {
            onStartGallery()
        })

        viewModel.failedGetOcrPictureEvent.observe(this@AddPrescriptionActivity, {
            dialog.dismiss()
            shortToast("지원하는 양식의 처방전이 아닙니다")
        })

        viewModel.selfAddPrescriptionEvent.observe(this@AddPrescriptionActivity, {
            onStartSelfAddAlarm()
        })

        repeatOnStart {
            viewModel.eventFlow.collect {
                when (it) {
                    is AddPrescriptionViewModel.MyEvent.IsSaveBitmap -> {
                        imgSave(it.bitmap)
                    }
                }
            }
        }

    }

    fun onStartCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.ssu_contest_eighteen_pomise.fileprovider",
                        photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                } else {
                    shortToast("사진 촬영 취소")
                }
            }
        }
    }

    fun onStartGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        filterActivityLauncher.launch(intent)
    }

    fun onStartSelfAddAlarm() {
        val intent = Intent(this, AddSelfNoOcrActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        slideNoneAndDownExit()
    }

    private fun requestPermissions() {
        requestMultiplePermission.launch(PERMISSIONS_REQUESTED)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    //Android Q (Android 10, API 29 이상에서는 이 메서드를 통해서 이미지를 저장한다.)
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageOnAboveAndroidQ(bitmap: Bitmap) {
        val fileName = System.currentTimeMillis().toString() + ".png" // 파일이름 현재시간.png

        /*
        * ContentValues() 객체 생성.
        * ContentValues는 ContentResolver가 처리할 수 있는 값을 저장해둘 목적으로 사용된다.
        * */
        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, saveTakePicturePath) // 경로 설정
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName) // 파일이름을 put해준다.
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1) // 현재 is_pending 상태임을 만들어준다.
            // 다른 곳에서 이 데이터를 요구하면 무시하라는 의미로, 해당 저장소를 독점할 수 있다.
        }

        // 이미지를 저장할 uri를 미리 설정해놓는다.
        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if (uri != null) {
                val image = contentResolver.openFileDescriptor(uri, "w", null)
                // write 모드로 file을 open한다.

                if (image != null) {
                    val fos = FileOutputStream(image.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    //비트맵을 FileOutputStream를 통해 compress한다.
                    fos.close()

                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0) // 저장소 독점을 해제한다.
                    contentResolver.update(uri, contentValues, null, null)
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveImageOnUnderAndroidQ(bitmap: Bitmap) {
        val fileName = System.currentTimeMillis().toString() + ".png"
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/${saveTakePicturePath}"
        val dir = File(path)

        if (dir.exists().not()) {
            dir.mkdirs() // 폴더 없을경우 폴더 생성
        }

        try {
            val fileItem = File("$dir/$fileName")
            fileItem.createNewFile()
            //0KB 파일 생성.

            val fos = FileOutputStream(fileItem) // 파일 아웃풋 스트림

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            //파일 아웃풋 스트림 객체를 통해서 Bitmap 압축.

            fos.close() // 파일 아웃풋 스트림 객체 close

            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileItem)))
            // 브로드캐스트 수신자에게 파일 미디어 스캔 액션 요청. 그리고 데이터로 추가된 파일에 Uri를 넘겨준다.
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendOcrBitmap(prescriptionImageBitmap: Bitmap, isSave: Boolean = false) {
        val byteArrayOutputStream = ByteArrayOutputStream()

        //해당 비트맵 크기를 줄이기 위해 압축해서 인자로 넘겨준 바이트배열아웃풋스트림으로 받음.
        prescriptionImageBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            99,
            byteArrayOutputStream
        )
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        val outStream = ByteArrayOutputStream()
        val res: Resources = resources
        val imageBase64 = Base64.encodeToString(byteArray, 0)
        dialog.show()
        viewModel.getOcrData(imageBase64, prescriptionImageBitmap, isSave)
        //이걸 일단 OCR 보내보기. 그리고 결과보고 받을 수 있으면 액티비티 실행.
    }

    //이미지 저장 버튼 클릭 메서드
    fun imgSave(bitmap: Bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //Q 버전 이상일 경우. (안드로이드 10, API 29 이상일 경우)
            saveImageOnAboveAndroidQ(bitmap)
            //shortToast("이미지 저장이 됐습니다.")
        } else {
            // Q 버전 이하일 경우. 저장소 쓰기 권한을 얻어온다.
            val writePermission =
                ActivityCompat.checkSelfPermission(this, PERMISSION_WRITE_EXTERNAL_STORAGE)

            if (writePermission == PackageManager.PERMISSION_GRANTED) {
                saveImageOnUnderAndroidQ(bitmap)
                //shortToast("이미지 저장이 됐습니다.")
            } else {
                //shortToast("이미지 저장 권한이 없습니다.")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK && currentPhotoPath != null) {
            val file = File(currentPhotoPath!!)
            val bitmap: Bitmap
            if (Build.VERSION.SDK_INT < 28) {
                val tempBitmap = MediaStore.Images.Media.getBitmap(
                    this.contentResolver,
                    Uri.fromFile(file)
                )
                bitmap = tempBitmap
            } else {
                val source =
                    ImageDecoder.createSource(this.contentResolver, Uri.fromFile(file))
                val tempBitmap = ImageDecoder.decodeBitmap(source)
                bitmap = tempBitmap
            }
            sendOcrBitmap(bitmap, true)
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            shortToast("사진 찍기 취소")
        }
    }

    fun repeatOnStart(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    companion object {
        private const val PERMISSION_CAMERA = Manifest.permission.CAMERA
        private const val PERMISSION_READ_EXTERNAL_STORAGE =
            Manifest.permission.READ_EXTERNAL_STORAGE
        private const val PERMISSION_WRITE_EXTERNAL_STORAGE =
            Manifest.permission.WRITE_EXTERNAL_STORAGE

        private val PERMISSIONS_REQUESTED = arrayOf(
            PERMISSION_CAMERA,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE
        )

        const val saveTakePicturePath = "DCIM/EighteenPromise"
        const val REQUEST_TAKE_PHOTO = 1
    }
}