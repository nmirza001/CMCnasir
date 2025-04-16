package cmc.backend.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a saved school with timestamp
 * @author Nasir Mirza
 * @version Apr 15, 2025
 */
public class SavedSchool {
    private University university;
    private LocalDateTime savedDate;
    
    /**
     * Creates a new SavedSchool with the current timestamp
     * @param university The university to save
     */
    public SavedSchool(University university) {
        this.university = university;
        this.savedDate = LocalDateTime.now();
    }
    
    /**
     * Creates a new SavedSchool with a specific timestamp
     * @param university The university to save
     * @param savedDate The timestamp when it was saved
     */
    public SavedSchool(University university, LocalDateTime savedDate) {
        this.university = university;
        this.savedDate = savedDate;
    }
    
    /**
     * @return The university that was saved
     */
    public University getUniversity() {
        return university;
    }
    
    /**
     * @return The date and time when the university was saved
     */
    public LocalDateTime getSavedDate() {
        return savedDate;
    }
    
    /**
     * @return A formatted string of the saved date/time
     */
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return savedDate.format(formatter);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        SavedSchool that = (SavedSchool) obj;
        return university.getName().equals(that.university.getName());
    }
    
    @Override
    public int hashCode() {
        return university.getName().hashCode();
    }
}