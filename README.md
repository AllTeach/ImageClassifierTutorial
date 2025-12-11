# Image Classifier Tutorial

## Overview
This project demonstrates how to build an **Image Classification App** using **TensorFlow Lite** on Android. The app uses a pre-trained TensorFlow Lite model to classify images into various categories. The app is compatible with modern versions of Android and demonstrates best practices for handling model I/O and preprocessing.

## Features
- Supports TensorFlow Lite model inference.
- Handles `UINT8` input and output tensors.
- Processes images dynamically from resources.
- Displays real-time inference results.

---

## Getting Started

### Prerequisites
- **Android Studio** (recommended version 2023.1 or later).
- **Java 8 or higher**.
- Basic knowledge of TensorFlow Lite.

### Clone the Repository
Clone this repository to your local machine using the following command:
```bash
git clone https://github.com/AllTeach/ImageClassifierTutorial.git
```

---

## Project Structure

Key components of the repository:
- **`app` directory**:
  Contains the app's source code, `build.gradle.kts`, and resources.
- **Gradle Wrapper**:
  - `gradlew` and `gradlew.bat`: Used to run Gradle commands even if Gradle is not installed locally.
  - `gradle/wrapper/`: Contains files for the Gradle wrapper configuration.
- **Project-Level Configuration:**
  - `build.gradle.kts`: Dependency definitions.
  - `settings.gradle.kts`: Gradle project settings.
  - `gradle.properties`: Configuration properties for Gradle builds.
- **`.idea/` Directory**:
  IntelliJ/Android Studio metadata.

---

## How to Run the App

Follow these steps to run the classifier app:

### Step 1: Open the Project
1. Launch **Android Studio**.
2. Open the cloned repository folder: `File > Open > Navigate to the project directory`.

### Step 2: Sync Gradle
- Android Studio will prompt you to sync Gradle files. Click **Sync Now**.

### Step 3: Debug or Build the App
1. Connect your Android device or start an emulator.
2. Run the app:
   - Click the **Run** button (green arrow).
   - Select your device or emulator.

### Step 4: Preloaded Image Classification
The app comes preloaded with a sample image (`sample_image.jpg`) located in the resources. It runs inference and fetches the result displayed on the screen.

---

## Implementation Details

### Input Tensor Requirements
- **Shape**: `[1, 224, 224, 3]`
- **Data Type**: `UINT8` (unsigned 8-bit integer values ranging from 0-255).

### Output Tensor Requirements
- **Shape**: `[1, NumClasses]`
- **Data Type**: `UINT8`. This is converted to probabilities (`FLOAT32`) by scaling the values to the range `[0.0, 1.0]`.

### Using the Pre-trained Model
- Place your TensorFlow Lite model (`model.tflite`) in the `assets` directory.
- Ensure the corresponding class labels file (`label_map.txt`) is also in the `assets` directory.
- The app dynamically processes the shape of tensors using the TensorFlow Lite interpreter.

---

## Troubleshooting

| Issue                                          | Cause                               | How to Fix                                                                 |
|------------------------------------------------|-------------------------------------|-----------------------------------------------------------------------------|
| `Input size mismatch`                         | ByteBuffer size doesn’t match model | Ensure input ByteBuffer is `1x224x224x3` and data is of type `UINT8`.      |
| `Output tensor type mismatch`                 | Wrong data type in output buffer    | Ensure output ByteBuffer properly maps UINT8 to FLOAT32 probabilities.    |
| `Application crashes on startup`              | Gradle sync issues                  | Rebuild the project and ensure all dependencies are correctly installed.   |

### Relevant Links
- [TensorFlow Lite Documentation](https://www.tensorflow.org/lite/)
- [Netron Model Viewer](https://netron.app/)
- [Android Developer Tools](https://developer.android.com/tools)

---

## License
This project is licensed under the [MIT License](LICENSE).