package com.example.imageclassifiertutorial;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Interpreter tflite;       // TensorFlow Lite Interpreter
    private List<String> labels;     // List of labels
    private static final int IMAGE_SIZE = 224; // Model input size

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI components
        ImageView imageView = findViewById(R.id.imageView);
        TextView resultTextView = findViewById(R.id.resultTextView);

        try {
            // Load the model and labels
            tflite = new Interpreter(loadModelFile());
            labels = loadLabels("labels.txt");

            // Load a sample image
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_2);
            imageView.setImageBitmap(bitmap);
            ByteBuffer inputBuffer = preprocessImage(bitmap);

            // Perform classification
            String result = classifyImage(inputBuffer);
            // Classify the image
            resultTextView.setText(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load the TensorFlow Lite model file
    private ByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // Custom method to load labels from assets
    private List<String> loadLabels(String fileName) throws IOException {
        List<String> labels = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
        String line;
        while ((line = reader.readLine()) != null) {
            labels.add(line);
        }
        reader.close();
        return labels;
    }

    // Preprocess the image and classify it
    private String classifyImage(ByteBuffer inputBuffer) {
        // Allocate output buffer for UINT8 output
        int outputSize = tflite.getOutputTensor(0).numBytes(); // Dynamically retrieve size
        ByteBuffer outputBuffer = ByteBuffer.allocateDirect(outputSize);
        outputBuffer.order(ByteOrder.nativeOrder());

        // Run inference
        tflite.run(inputBuffer, outputBuffer);

        // Convert UINT8 output to FLOAT32
        outputBuffer.rewind(); // Reset position of ByteBuffer
        float[] probabilities = new float[labels.size()];
        for (int i = 0; i < labels.size(); i++) {
            probabilities[i] = (outputBuffer.get() & 0xFF) / 255.0f; // UINT8 -> FLOAT32
        }

        // Find the label with the highest probability
        int maxIndex = 0;
        for (int i = 1; i < probabilities.length; i++) {
            if (probabilities[i] > probabilities[maxIndex]) {
                maxIndex = i;
            }
        }

        // Return the result as "Label (Confidence %)"
        return labels.get(maxIndex) + " (" + (probabilities[maxIndex] * 100) + "%)";
    }


    // Preprocess the image to prepare the input for TensorFlow Lite model
    private ByteBuffer preprocessImage(Bitmap bitmap) {
        // Resize the bitmap to 224x224 (width x height) as required by the model
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

        // Allocate a ByteBuffer: Batch size (1) × 224 × 224 × 3 channels (RGB) × 1 byte per value (UINT8)
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(1 * 224 * 224 * 3);
        inputBuffer.order(ByteOrder.nativeOrder());

        // Fill the ByteBuffer with raw pixel values (UINT8)
        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 224; x++) {
                // Get RGB values from the resized bitmap
                int pixel = resizedBitmap.getPixel(x, y);
                int r = (pixel >> 16) & 0xFF; // Extract red
                int g = (pixel >> 8) & 0xFF;  // Extract green
                int b = pixel & 0xFF;         // Extract blue

                // Add raw RGB values to the buffer
                inputBuffer.put((byte) r); // Add red value
                inputBuffer.put((byte) g); // Add green value
                inputBuffer.put((byte) b); // Add blue value
            }
        }

        return inputBuffer;
    }
    private void validateInputSize(ByteBuffer inputBuffer) {
        // Get the expected size from TensorFlow Lite
        int expectedSize = tflite.getInputTensor(0).numBytes();
        if (inputBuffer.capacity() != expectedSize) {
            throw new IllegalArgumentException("Input size mismatch! Buffer size: " +
                    inputBuffer.capacity() + " bytes, expected: " + expectedSize + " bytes");
        }
    }

    private void logModelInputShape() {
        int[] inputShape = tflite.getInputTensor(0).shape(); // Get shape: [batch, height, width, channels]
        String dataType = tflite.getInputTensor(0).dataType().toString(); // Get data type of tensor
        Log.d("ModelInputShape", "Model expects input with shape: " +
                java.util.Arrays.toString(inputShape) + " and data type: " + dataType);
    }

}