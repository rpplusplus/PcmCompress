#include <LameEncoder.h>
#include "lame.h"
#include <android/log.h>

static lame_t lame;
const int MP3_SIZE = 16*1024;
const int SAMPLE_RATE = 44100;
const int BIT_RATE = 128;
const int QUALITY = 5;

#define LOG_TAG "liblameutl"
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


JNIEXPORT void JNICALL Java_com_tietie_postcard_func_LameEncoder_onCreate(JNIEnv *env, jobject c)
{
	lame = lame_init();
	lame_set_in_samplerate(lame, SAMPLE_RATE);
	lame_set_VBR(lame, vbr_default);
	lame_init_params(lame);

	// lame_set_in_samplerate(lame, SAMPLE_RATE);
	// lame_set_num_channels(lame, 1);
	// lame_set_out_samplerate(lame, SAMPLE_RATE);
	// lame_set_brate(lame, BIT_RATE);
	// lame_set_quality(lame, QUALITY);
	// lame_init_params(lame);

	LOGI("onCreate Finished");
}

JNIEXPORT jint JNICALL Java_com_tietie_postcard_func_LameEncoder_onEncode(JNIEnv *env, jobject c, jshortArray input_buffer, jbyteArray output_buffer)
{
	jshort * array_body = (*env)->GetShortArrayElements(env, input_buffer,0);
	jsize input_len = (*env)->GetArrayLength(env, input_buffer);
    
	short * c_input_buffer = (short *)array_body;
	unsigned char mp3_buffer[MP3_SIZE];
    
	jint output_length = lame_encode_buffer_interleaved(lame, c_input_buffer, input_len, mp3_buffer, MP3_SIZE);
	jbyte *by = (jbyte*)mp3_buffer;
	(*env)->SetByteArrayRegion(env, output_buffer, 0, output_length, by);

	LOGI("onEncode Finish input len = %d output len = %d", input_len, output_length);
	return output_length;
}

JNIEXPORT jint JNICALL Java_com_tietie_postcard_func_LameEncoder_onFinish(JNIEnv * env, jobject c, jbyteArray output_buffer)
{
	unsigned char mp3_buffer[MP3_SIZE];
	jint output_length = lame_encode_flush(lame, mp3_buffer, MP3_SIZE);
	jbyte *by = (jbyte*)mp3_buffer;
	(*env)->SetByteArrayRegion(env, output_buffer, 0, output_length, by);

	LOGI("onFinish Finish %d", output_length);
	return output_length;
}

JNIEXPORT void JNICALL Java_com_tietie_postcard_func_LameEncoder_onDestory(JNIEnv *env, jobject c)
{
	lame_close(lame);
	LOGI("onDestory Finish");
}