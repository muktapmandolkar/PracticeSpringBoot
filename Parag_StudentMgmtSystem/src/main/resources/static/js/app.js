// API Base URL
const API_BASE_URL = 'http://localhost:8090/api/students';

// Global variables
let currentStudents = [];

// DOM Elements
const sections = document.querySelectorAll('.section');
const navLinks = document.querySelectorAll('.nav-link');

// Navigation
navLinks.forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const targetId = link.getAttribute('href').substring(1);
        showSection(targetId);
        
        // Update active nav link
        navLinks.forEach(l => l.classList.remove('active'));
        link.classList.add('active');
    });
});

// Show specific section
function showSection(sectionId) {
    sections.forEach(section => {
        section.classList.remove('active');
        if (section.id === sectionId) {
            section.classList.add('active');
        }
    });
}

// Show loading spinner
function showLoading() {
    document.getElementById('loadingSpinner').style.display = 'flex';
}

// Hide loading spinner
function hideLoading() {
    document.getElementById('loadingSpinner').style.display = 'none';
}

// Show toast notification
function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    const toastMessage = document.getElementById('toastMessage');
    
    toastMessage.textContent = message;
    toast.classList.add('show');
    
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// Load all students
async function loadStudents() {
    showLoading();
    try {
        const response = await fetch(API_BASE_URL);
        if (!response.ok) throw new Error('Failed to fetch students');
        
        const students = await response.json();
        currentStudents = students;
        renderStudentsTable(students);
        updateDashboardStats(students);
        hideLoading();
    } catch (error) {
        console.error('Error loading students:', error);
        showToast('Error loading students: ' + error.message, 'error');
        hideLoading();
    }
}

// Render students table
function renderStudentsTable(students) {
    const tableBody = document.getElementById('studentsTableBody');
    const noDataMessage = document.getElementById('noStudentsMessage');
    
    if (students.length === 0) {
        tableBody.innerHTML = '';
        noDataMessage.style.display = 'flex';
        return;
    }
    
    noDataMessage.style.display = 'none';
    
    tableBody.innerHTML = students.map(student => `
        <tr>
            <td>${student.id}</td>
            <td>${student.firstName} ${student.lastName}</td>
            <td>${student.email}</td>
            <td>${student.course}</td>
            <td>Year ${student.year}</td>
            <td>
                <span class="gpa-badge ${getGPAClass(student.gpa)}">
                    ${student.gpa.toFixed(2)}
                </span>
            </td>
            <td>
                <span class="status-badge ${student.active ? 'status-active' : 'status-inactive'}">
                    ${student.active ? 'Active' : 'Inactive'}
                </span>
            </td>
            <td class="action-buttons-cell">
                <button class="btn-primary" onclick="editStudent(${student.id})">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn-danger" onclick="deleteStudent(${student.id})">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

// Get GPA class for styling
function getGPAClass(gpa) {
    if (gpa >= 3.5) return 'excellent';
    if (gpa >= 3.0) return 'good';
    if (gpa >= 2.5) return 'average';
    return 'below';
}

// Update dashboard statistics
function updateDashboardStats(students) {
    const totalStudents = students.length;
    const activeStudents = students.filter(s => s.active).length;
    const averageGPA = students.length > 0 
        ? (students.reduce((sum, s) => sum + s.gpa, 0) / students.length).toFixed(2)
        : '0.00';
    
    document.getElementById('totalStudents').textContent = totalStudents;
    document.getElementById('activeStudents').textContent = activeStudents;
    document.getElementById('averageGPA').textContent = averageGPA;
}

// Search students
async function searchStudents() {
    const searchTerm = document.getElementById('searchInput').value.trim();
    
    if (!searchTerm) {
        loadStudents();
        return;
    }
    
    showLoading();
    try {
        const response = await fetch(`${API_BASE_URL}/search?name=${encodeURIComponent(searchTerm)}`);
        if (!response.ok) throw new Error('Search failed');
        
        const students = await response.json();
        renderStudentsTable(students);
        hideLoading();
    } catch (error) {
        console.error('Error searching students:', error);
        showToast('Error searching students', 'error');
        hideLoading();
    }
}

// Add student form submission
document.getElementById('addStudentForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const studentData = {
        firstName: document.getElementById('firstName').value.trim(),
        lastName: document.getElementById('lastName').value.trim(),
        email: document.getElementById('email').value.trim(),
        phone: document.getElementById('phone').value.trim(),
        course: document.getElementById('course').value,
        year: parseInt(document.getElementById('year').value),
        gpa: parseFloat(document.getElementById('gpa').value)
    };
    
    // Validate data
    if (!validateStudentData(studentData)) return;
    
    showLoading();
    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(studentData)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Failed to add student');
        }
        
        const newStudent = await response.json();
        showToast('Student added successfully!');
        
        // Reset form and reload students
        document.getElementById('addStudentForm').reset();
        loadStudents();
        showSection('students');
        
        hideLoading();
    } catch (error) {
        console.error('Error adding student:', error);
        showToast('Error: ' + error.message, 'error');
        hideLoading();
    }
});

// Validate student data
function validateStudentData(data) {
    let isValid = true;
    
    // Clear previous errors
    document.querySelectorAll('.error').forEach(el => el.textContent = '');
    
    if (!data.firstName || data.firstName.length < 2) {
        document.getElementById('firstNameError').textContent = 'First name must be at least 2 characters';
        isValid = false;
    }
    
    if (!data.lastName || data.lastName.length < 2) {
        document.getElementById('lastNameError').textContent = 'Last name must be at least 2 characters';
        isValid = false;
    }
    
    if (!data.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(data.email)) {
        document.getElementById('emailError').textContent = 'Please enter a valid email';
        isValid = false;
    }
    
    if (data.phone && !/^\d{10}$/.test(data.phone)) {
        document.getElementById('phoneError').textContent = 'Phone must be 10 digits';
        isValid = false;
    }
    
    if (!data.course) {
        document.getElementById('courseError').textContent = 'Please select a course';
        isValid = false;
    }
    
    if (!data.year || data.year < 1 || data.year > 5) {
        document.getElementById('yearError').textContent = 'Please select a valid year';
        isValid = false;
    }
    
    if (!data.gpa || data.gpa < 0 || data.gpa > 4) {
        document.getElementById('gpaError').textContent = 'GPA must be between 0.0 and 4.0';
        isValid = false;
    }
    
    return isValid;
}

// Update GPA from slider
function updateGPA(value) {
    document.getElementById('gpa').value = value;
}

// Edit student
async function editStudent(id) {
    showLoading();
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`);
        if (!response.ok) throw new Error('Failed to fetch student');
        
        const student = await response.json();
        openEditModal(student);
        hideLoading();
    } catch (error) {
        console.error('Error fetching student:', error);
        showToast('Error loading student details', 'error');
        hideLoading();
    }
}

// Open edit modal
function openEditModal(student) {
    const modalBody = document.getElementById('editModalBody');
    
    modalBody.innerHTML = `
        <form id="editStudentForm" onsubmit="updateStudent(event, ${student.id})">
            <div class="form-row">
                <div class="form-group">
                    <label>First Name</label>
                    <input type="text" id="editFirstName" value="${student.firstName}" required>
                </div>
                <div class="form-group">
                    <label>Last Name</label>
                    <input type="text" id="editLastName" value="${student.lastName}" required>
                </div>
            </div>
            
            <div class="form-group">
                <label>Email</label>
                <input type="email" id="editEmail" value="${student.email}" required>
            </div>
            
            <div class="form-group">
                <label>Phone</label>
                <input type="tel" id="editPhone" value="${student.phone || ''}">
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label>Course</label>
                    <select id="editCourse" required>
                        <option value="Computer Science" ${student.course === 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                        <option value="Electrical Engineering" ${student.course === 'Electrical Engineering' ? 'selected' : ''}>Electrical Engineering</option>
                        <option value="Mechanical Engineering" ${student.course === 'Mechanical Engineering' ? 'selected' : ''}>Mechanical Engineering</option>
                        <option value="Civil Engineering" ${student.course === 'Civil Engineering' ? 'selected' : ''}>Civil Engineering</option>
                        <option value="Business Administration" ${student.course === 'Business Administration' ? 'selected' : ''}>Business Administration</option>
                        <option value="Medicine" ${student.course === 'Medicine' ? 'selected' : ''}>Medicine</option>
                        <option value="Law" ${student.course === 'Law' ? 'selected' : ''}>Law</option>
                        <option value="Arts" ${student.course === 'Arts' ? 'selected' : ''}>Arts</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Year</label>
                    <select id="editYear" required>
                        <option value="1" ${student.year === 1 ? 'selected' : ''}>First Year</option>
                        <option value="2" ${student.year === 2 ? 'selected' : ''}>Second Year</option>
                        <option value="3" ${student.year === 3 ? 'selected' : ''}>Third Year</option>
                        <option value="4" ${student.year === 4 ? 'selected' : ''}>Fourth Year</option>
                        <option value="5" ${student.year === 5 ? 'selected' : ''}>Fifth Year</option>
                    </select>
                </div>
            </div>
            
            <div class="form-group">
                <label>GPA</label>
                <input type="number" id="editGpa" step="0.1" min="0" max="4" value="${student.gpa}" required>
            </div>
            
            <div class="form-group">
                <label>Status</label>
                <select id="editActive">
                    <option value="true" ${student.active ? 'selected' : ''}>Active</option>
                    <option value="false" ${!student.active ? 'selected' : ''}>Inactive</option>
                </select>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn-primary">Update Student</button>
                <button type="button" class="btn-secondary" onclick="closeModal()">Cancel</button>
            </div>
        </form>
    `;
    
    document.getElementById('editModal').style.display = 'flex';
}

// Update student
async function updateStudent(e, id) {
    e.preventDefault();
    
    const studentData = {
        firstName: document.getElementById('editFirstName').value.trim(),
        lastName: document.getElementById('editLastName').value.trim(),
        email: document.getElementById('editEmail').value.trim(),
        phone: document.getElementById('editPhone').value.trim(),
        course: document.getElementById('editCourse').value,
        year: parseInt(document.getElementById('editYear').value),
        gpa: parseFloat(document.getElementById('editGpa').value),
        active: document.getElementById('editActive').value === 'true'
    };
    
    showLoading();
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(studentData)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Failed to update student');
        }
        
        showToast('Student updated successfully!');
        closeModal();
        loadStudents();
        hideLoading();
    } catch (error) {
        console.error('Error updating student:', error);
        showToast('Error: ' + error.message, 'error');
        hideLoading();
    }
}

// Delete student
async function deleteStudent(id) {
    if (!confirm('Are you sure you want to delete this student?')) return;
    
    showLoading();
    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Failed to delete student');
        
        showToast('Student deleted successfully!');
        loadStudents();
        hideLoading();
    } catch (error) {
        console.error('Error deleting student:', error);
        showToast('Error deleting student', 'error');
        hideLoading();
    }
}

// Close modal
function closeModal() {
    document.getElementById('editModal').style.display = 'none';
}

// Load statistics
async function loadStatistics() {
    showLoading();
    try {
        const response = await fetch(`${API_BASE_URL}/statistics`);
        if (!response.ok) throw new Error('Failed to load statistics');
        
        const stats = await response.json();
        
        document.getElementById('statTotal').textContent = stats.totalStudents;
        document.getElementById('statActive').textContent = stats.activeStudents;
        document.getElementById('statGPA').textContent = stats.averageGPA.toFixed(2);
        document.getElementById('statTimestamp').textContent = stats.timestamp;
        
        // Update GPA distribution (mock data for now)
        updateGPADistribution();
        
        hideLoading();
        showToast('Statistics updated!');
    } catch (error) {
        console.error('Error loading statistics:', error);
        showToast('Error loading statistics', 'error');
        hideLoading();
    }
}

// Update GPA distribution (mock function)
function updateGPADistribution() {
    // This is a mock function - in a real app, you'd calculate actual distribution
    const excellent = Math.floor(Math.random() * 30) + 20;
    const good = Math.floor(Math.random() * 30) + 20;
    const average = Math.floor(Math.random() * 30) + 20;
    const below = Math.floor(Math.random() * 30) + 20;
    
    const total = excellent + good + average + below;
    
    document.getElementById('gpaExcellent').style.width = `${(excellent/total)*100}%`;
    document.getElementById('gpaGood').style.width = `${(good/total)*100}%`;
    document.getElementById('gpaAverage').style.width = `${(average/total)*100}%`;
    document.getElementById('gpaBelow').style.width = `${(below/total)*100}%`;
}

// Initialize the application
async function init() {
    // Set up event listeners
    document.getElementById('searchInput').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') searchStudents();
    });
    
    // Load initial data
    await loadStudents();
    await loadStatistics();
    
    // Show welcome message
    setTimeout(() => {
        showToast('Welcome to Student Management System!');
    }, 1000);
}

// Start the application when DOM is loaded
document.addEventListener('DOMContentLoaded', init);

// Close modal when clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('editModal');
    if (event.target === modal) {
        closeModal();
    }
}