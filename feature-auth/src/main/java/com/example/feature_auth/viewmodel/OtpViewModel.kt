package com.example.feature_auth.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.base.AbstractMviViewModel
import com.example.domain.model.PhoneNumber
import com.example.feature_auth.contract.OtpContract
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update

private const val OTP_CODE_LENGTH = 4


class OtpViewModel :
    AbstractMviViewModel<OtpContract.ViewIntent, OtpContract.ViewState, OtpContract.SingleEvent>() {

    private val _viewState = MutableStateFlow(OtpContract.ViewState())
    override val viewState: StateFlow<OtpContract.ViewState>
        get() = _viewState

    private val otpFlow = MutableStateFlow("")
    private val phoneNumber = MutableStateFlow(PhoneNumber.EMPTY)
    private val timerFlow = MutableSharedFlow<Int>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        intentSharedFlow.onEach { newIntent ->
            when (newIntent) {
                is OtpContract.ViewIntent.SetupPhoneNumber -> {
                    phoneNumber.value = newIntent.phoneNumber
                    startTimer()
                }

                is OtpContract.ViewIntent.OnOtpEntered -> {
                    val newOtp = newIntent.value
                    if (newOtp.length < OTP_CODE_LENGTH) return@onEach
                    otpFlow.value = newOtp
                }

                is OtpContract.ViewIntent.OnResendClicked -> {
                }
            }
        }.shareIn(viewModelScope, SharingStarted.Eagerly)

        timerFlow.onEach { time ->
            _viewState.update {
                it.copy(timeLeft = time)
            }
        }.launchIn(viewModelScope)
    }

    private fun startTimer() {
        (10 downTo 0)
            .asSequence()
            .asFlow()
            .onEach { time ->
                timerFlow.tryEmit(time)
                delay(1_000)
            }
            .launchIn(viewModelScope)
    }


}