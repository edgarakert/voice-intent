package com.example.voiceintent.feature.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note.domain.use_case.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(NotesState())
    val state: StateFlow<NotesState> = _state.asStateFlow()

    init {
        observeNotes()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeNotes() {
        combine(
            _state.map { it.searchQuery }.distinctUntilChanged().debounce(300),
            _state.map { it.selectedMoodFilter }.distinctUntilChanged()
        ) { query, moodFilter -> query to moodFilter }
            .flatMapLatest { (query, moodFilter) ->
                getNotesUseCase.invoke(query, moodFilter.value)
            }
            .onEach { notes ->
                _state.update { current ->
                    val filtered = applyTagFilter(notes, current.selectedTagsFilter)
                    val allTags = notes.flatMap { it.tags }.distinct().sorted()
                    current.copy(
                        notes = notes,
                        filteredNotes = filtered,
                        allTags = allTags,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun applyTagFilter(notes: List<Note>, tagFilter: String?) =
        if (tagFilter == null) notes
        else notes.filter { it.tags.contains(tagFilter) }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onMoodFilterChanged(moodFilter: MoodFilter) {
        _state.update { it.copy(selectedMoodFilter = moodFilter) }
    }

    fun onTagFilterChanged(tagFilter: String?) {
        _state.update { current ->
            current.copy(
                selectedTagsFilter = tagFilter,
                filteredNotes = applyTagFilter(current.notes, tagFilter)
            )
        }
    }

    fun onClearFilters() {
        _state.update { current ->
            current.copy(
                searchQuery = "",
                selectedMoodFilter = MoodFilter.ALL,
                selectedTagsFilter = null,
                filteredNotes = current.notes
            )
        }
    }
}