package com.example.myapplication_ass2

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication_ass2.di.DrugContainer
import com.example.myapplication_ass2.network.FakeDrugApiService
import com.example.myapplication_ass2.repository.FakeDrugRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [28])
class ViewModelTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var context: Context
    private lateinit var application: Application
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        context = ApplicationProvider.getApplicationContext()
        application = ApplicationProvider.getApplicationContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .fallbackToDestructiveMigration()
            .build()
        DrugContainer.repository = FakeDrugRepository(apiService = FakeDrugApiService())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        appDatabase.close()
    }

    @Test
    fun testMedicalAdviceViewModel_fetchDrugAdvice_success() = runTest {
        val viewModel = MedicalAdviceViewModel()
        viewModel.fetchDrugAdvice("aspirin")

        advanceUntilIdle()

        val drugInfo = viewModel.drugInfo.first { it != null }
        val error = viewModel.error.value

        assertNotNull("drugInfo should not be null", drugInfo)
        assertTrue("drugInfo list should not be empty", drugInfo!!.isNotEmpty())
        assertEquals("Drug name should be 'aspirin'", "aspirin", drugInfo.first().name)
        assertNull("error should be null", error)
    }

    class TestMedicationViewModel(application: Application, private val appDatabase: AppDatabase) :
        AndroidViewModel(application) {
        private val medicationDao = appDatabase.medicationDao()
        private val _medications = MutableStateFlow<List<MedicationEntity>>(emptyList())
        val medications = _medications

        init {
            viewModelScope.launch {
                medicationDao.getAllMedications().collect { meds ->
                    _medications.value = meds
                }
            }
        }

        fun markAsDone(medication: MedicationEntity) {
            viewModelScope.launch {
                val updatedMed = medication.copy(taken = true)
                medicationDao.updateMedication(updatedMed)
            }
        }

        fun addMedication(name: String, dosage: String, time: String) {
            viewModelScope.launch {
                val med = MedicationEntity(name = name, dosage = dosage, time = time)
                medicationDao.insertMedication(med)
            }
        }
    }

    class TestRoutineTaskViewModel(application: Application, private val appDatabase: AppDatabase) :
        AndroidViewModel(application) {
        private val routineTaskDao = appDatabase.routineTaskDao()
        private val _tasks = MutableStateFlow<List<RoutineTaskEntity>>(emptyList())
        val tasks = _tasks

        init {
            viewModelScope.launch {
                routineTaskDao.getAllTasks().collect { list ->
                    _tasks.value = list
                }
            }
        }

        fun addTask(name: String, description: String, time: String) {
            viewModelScope.launch {
                val task = RoutineTaskEntity(name = name, description = description, time = time)
                routineTaskDao.insertTask(task)
            }
        }

        fun deleteTask(task: RoutineTaskEntity) {
            viewModelScope.launch {
                routineTaskDao.deleteTask(task)
            }
        }
    }

    @Test
    fun testTestMedicationViewModel_addAndMarkDone() = runTest {
        val viewModel = TestMedicationViewModel(application, appDatabase)
        val dao = appDatabase.medicationDao()

        viewModel.addMedication("Paracetamol", "500mg", "08:00")
        advanceUntilIdle()

        val medsAfterInsert = dao.getAllMedications().first()
        assertEquals("Medication count should increase", 1, medsAfterInsert.size)

        val newMed = medsAfterInsert.find { it.name == "Paracetamol" }
        assertNotNull("New medication should exist", newMed)
        assertFalse("New medication should not be taken", newMed!!.taken)

        viewModel.markAsDone(newMed)
        advanceUntilIdle()

        val medsAfterUpdate = dao.getAllMedications().first()
        val updatedMed = medsAfterUpdate.find { it.name == "Paracetamol" }
        assertNotNull("Updated medication should exist", updatedMed)
        assertTrue("Medication should be marked as taken", updatedMed!!.taken)
    }

    @Test
    fun testTestRoutineTaskViewModel_addAndDeleteTask() = runTest {
        val viewModel = TestRoutineTaskViewModel(application, appDatabase)
        val dao = appDatabase.routineTaskDao()

        viewModel.addTask("Exercise", "Morning run", "07:00")
        advanceUntilIdle()

        val tasksAfterAdd = dao.getAllTasks().first()
        assertEquals("Task count should increase", 1, tasksAfterAdd.size)

        val newTask = tasksAfterAdd.find { it.name == "Exercise" }
        assertNotNull("New task should exist", newTask)

        viewModel.deleteTask(newTask!!)
        advanceUntilIdle()

        val tasksAfterDelete = dao.getAllTasks().first()
        assertEquals("Task count should return to 0", 0, tasksAfterDelete.size)
    }

    @Test
    fun testAppSettingsViewModel() {
        val viewModel = AppSettingsViewModel()
        assertEquals("Default font size should be 16f", 16f, viewModel.fontSize.value)
        assertFalse("Default should not be dark mode", viewModel.isDarkMode.value)

        viewModel.fontSize.value = 20f
        viewModel.isDarkMode.value = true

        assertEquals("Font size should update to 20f", 20f, viewModel.fontSize.value)
        assertTrue("Dark mode should be true", viewModel.isDarkMode.value)
    }
}
