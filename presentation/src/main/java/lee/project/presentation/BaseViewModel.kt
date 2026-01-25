package lee.project.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

/**
 * MVI 패턴 제어를 위한 기본 BaseViewModel
 * UIState : 화면 상태
 * UiEvent : 화면 이벤트(행동)
 * UiEffect : 화면 효과(1회성 작업)
 */
abstract class BaseViewModel<S : UiState, E : UiEvent, F : UiEffect>(
    initialState: S
) : ViewModel() {

    protected val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    // UI -> VM (intent)
    private val _event = MutableSharedFlow<E>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    // VM -> UI (one-shot)
    private val _effect = Channel<F>(capacity = Channel.BUFFERED)
    val effect: Flow<F> = _effect.receiveAsFlow()

    init {
        _event
            .onEach { reduce(it) }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: E) {
        // 실패해도 최신 의도를 유지하는 쪽(DROP_OLDEST)
        _event.tryEmit(event)
    }

    protected fun setState(reducer: S.() -> S) {
        _uiState.update { it.reducer() }
    }

    protected suspend fun sendEffect(builder: () -> F) {
        _effect.send(builder())
    }

    protected abstract suspend fun reduce(event: E)
}

interface UiState
interface UiEvent
interface UiEffect