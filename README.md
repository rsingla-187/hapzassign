# HapzAssign Android App

## Overview

The HapzAssign Android app is a task management application that allows users to create, view, and edit events. The app features a user-friendly interface for managing events with functionalities including date and time pickers and event searching.

## Features

- **Event List**: View all events in a list with sorting by date and time.
- **Event Creation**: Create new events with details including name, location, description, date, time, and participants.
- **Event Editing**: Edit existing events by selecting them from the event list.
- **Event Detail View**: View detailed information about each event.
- **Search Functionality**: Filter events by name using a search bar.

## Architecture

The app follows a standard MVVM (Model-View-ViewModel) architecture to separate concerns and promote maintainability.

### Components

- **Model**: Represents the data structure of the events and handles data operations through Room Database.
- **View**: XML layout files representing the UI components. Activities and Fragments display the UI.
- **ViewModel**: Provides data to the View and handles user interaction logic. It communicates with the Repository to fetch or update data.
- **Repository**: Handles data operations and provides a clean API for data access to the ViewModel.
- **Database**: Uses Room to persist event data.

### Libraries and Tools

- **Room**: For local database management.
- **ViewModel**: For managing UI-related data in a lifecycle-conscious way.
- **LiveData**: For observing data changes.
- **Navigation Component**: For handling in-app navigation.
- **Material Components**: For UI elements like Floating Action Button and SearchView.

## Building and Running the App

### Prerequisites

- Android Studio
- Java 8 or higher
- Android SDK

### Instructions

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-username/hapzassign.git
