package com.example.voiceintent.feature.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note.domain.use_case.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(NotesState())
    val state: StateFlow<NotesState> = _state.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val notesFlow: Flow<PagingData<Note>> = combine(
        _state.map { it.searchQuery }.distinctUntilChanged().debounce(300),
        _state.map { it.selectedMoodFilter }.distinctUntilChanged(),
        _state.map { it.selectedTagsFilter }.distinctUntilChanged()
    ) { query, moodFilter, tagFilter -> Triple(query, moodFilter, tagFilter) }
        .flatMapLatest { (query, moodFilter, tagFilter) ->
            getNotesUseCase.invoke(query, moodFilter.value, tagFilter)
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onMoodFilterChanged(moodFilter: MoodFilter) {
        _state.update { it.copy(selectedMoodFilter = moodFilter) }
    }

    fun onTagFilterChanged(tagFilter: String?) {
        _state.update { it.copy(selectedTagsFilter = tagFilter) }
    }

    fun onClearFilters() {
        _state.update {
            it.copy(
                searchQuery = "",
                selectedMoodFilter = MoodFilter.ALL,
                selectedTagsFilter = null
            )
        }
    }
}