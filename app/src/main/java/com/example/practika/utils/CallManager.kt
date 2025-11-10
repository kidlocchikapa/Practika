package com.example.practika.utils

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.telecom.VideoProfile
import com.example.practika.services.CallConnectionService

object CallManager {
    fun registerPhoneAccount(context: Context) {
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val componentName = ComponentName(context, CallConnectionService::class.java)
        val phoneAccountHandle = PhoneAccountHandle(componentName, "PractikaCall")
        
        val phoneAccount = PhoneAccount.builder(phoneAccountHandle, "Practika Call")
            .setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
            .build()
            
        telecomManager.registerPhoneAccount(phoneAccount)
    }
    
    fun makeCall(context: Context, number: String) {
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val componentName = ComponentName(context, CallConnectionService::class.java)
        val phoneAccountHandle = PhoneAccountHandle(componentName, "PractikaCall")
        
        val extras = Bundle().apply {
            putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle)
            putInt(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, VideoProfile.STATE_AUDIO_ONLY)
        }
        
        val uri = Uri.fromParts("tel", number, null)
        telecomManager.placeCall(uri, extras)
    }
}