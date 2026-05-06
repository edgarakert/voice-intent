package com.example.voiceintent.feature.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voiceintent.feature.note.domain.entity.Note
import com.example.voiceintent.feature.note.domain.use_case.GetNotesUseCase
import com.example.voiceintent.feature.note.domain.use_case.SearchNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val getNotesUseCase: GetNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(NotesState())
    val state: StateFlow<NotesState> = _state.asStateFlow()

    init {
        observeNotes()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeNotes() {
        val searchFlow = _state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(300)
            .flatMapLatest { query ->
                if (query.isBlank()) getNotesUseCase.invoke()
                else searchNotesUseCase.invoke(query = query)
            }

        searchFlow.onEach { notes ->
            _state.update { current ->
                val filtered = applyFilters(
                    notes = notes,
                    moodFilter = current.selectedMoodFilter,
                    tagFilter = current.selectedTagsFilter
                )

                val allTags = notes
                    .flatMap { it.tags }
                    .distinct()
                    .sorted()

                current.copy(
                    notes = notes,
                    filteredNotes = filtered,
                    allTags = allTags,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun applyFilters(
        notes: List<Note>,
        moodFilter: MoodFilter,
        tagFilter: String?
    ) = notes
        .filter { note ->
            moodFilter.value == null || note.mood == moodFilter.value
        }
        .filter { note ->
            tagFilter == null || note.tags.contains(tagFilter)
        }

    fun onSearchQueryChanged(query: String) {
        _state.update {
            it.copy(searchQuery = query)
        }
    }

    fun onMoodFilterChanged(moodFilter: MoodFilter) {
        _state.update { current ->
            val filtered = applyFilters(
                notes = current.notes,
                moodFilter = moodFilter,
                tagFilter = current.selectedTagsFilter
            )
            current.copy(
                selectedMoodFilter = moodFilter,
                filteredNotes = filtered
            )
        }
    }

    fun onTagFilterChanged(tagFilter: String?) {
        _state.update { current ->
            val filtered = applyFilters(
                notes = current.notes,
                moodFilter = current.selectedMoodFilter,
                tagFilter = tagFilter
            )
            current.copy(
                selectedTagsFilter = tagFilter,
                filteredNotes = filtered
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