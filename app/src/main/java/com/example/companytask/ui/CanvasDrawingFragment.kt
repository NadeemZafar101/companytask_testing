package com.example.companytask.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.companytask.R
import com.example.companytask.databinding.FragmentCanvasDrawingBinding
import com.example.companytask.ui.helper.DrawView
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CanvasDrawingFragment : Fragment(){
    private lateinit var binding: FragmentCanvasDrawingBinding
    private lateinit var paint: DrawView
    private lateinit var saveBtn: ImageButton
    private lateinit var colourBtn: ImageButton
    private lateinit var strokeBtn: ImageButton
    private lateinit var undoBtn: ImageButton
    private lateinit var eraserBtn: ImageButton
    private lateinit var clearBtn: ImageButton
    private lateinit var redoBtn: ImageButton
    private lateinit var rangeSlider: SeekBar
    private var backgroundColour: Int = Color.WHITE
    private var currentColour: Int = Color.BLACK
    private lateinit var hideBtn: Button
    private lateinit var zoomBtn: Button
    private var toolsHidden: Boolean = false
    private var isExtras: Boolean = false
    private var isBackground: Boolean = false
    companion object
    {
        var isZoom: Boolean = false
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCanvasDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paint = binding.drawView
        hideBtn = binding.hideBtn
        rangeSlider = binding.rangebar
        undoBtn = binding.undoBtn
        saveBtn = binding.saveBtn
        colourBtn = binding.colourBtn
        strokeBtn = binding.strokeBtn
        eraserBtn = binding.eraserBtn
        clearBtn = binding.clearBtn
        redoBtn = binding.redoBtn
        backgroundColour = Color.WHITE
        currentColour = Color.BLACK

        setUpButtons()
    }
    private fun setUpButtons() {
        redoBtn.setOnClickListener {
            paint.redo()
        }
        undoBtn.setOnClickListener {
            paint.undo()
        }
        clearBtn.setOnClickListener {
            val newDialog = AlertDialog.Builder(requireContext())
            newDialog.setTitle("Clear Drawing")
            newDialog.setMessage("Clear your canvas? This will erase all of your progress.")
            newDialog.setPositiveButton("Yes") { dialog, which ->
                Toast.makeText(requireContext(), "Cleared the canvas!", Toast.LENGTH_SHORT).show()
                paint.clearCanvas()
                dialog.dismiss()
            }
            newDialog.setNegativeButton("No") { dialog, which -> dialog.cancel() }
            newDialog.show()
        }
        eraserBtn.setOnClickListener {
            paint.setEraser(true)
            paint.setColour(backgroundColour)
        }
        hideBtn.setOnClickListener {
            if (!toolsHidden) {
                hideBtn.text = "Show"
                toolsHidden = true
                redoBtn.visibility = View.GONE
                undoBtn.visibility = View.GONE
                saveBtn.visibility = View.GONE
                colourBtn.visibility = View.GONE
                strokeBtn.visibility = View.GONE
                eraserBtn.visibility = View.GONE
                clearBtn.visibility = View.GONE
                rangeSlider.visibility = View.GONE
                binding.bgMenu.setBackgroundColor(0)
            } else {
                hideBtn.text = "Hide"
                toolsHidden = false
                redoBtn.visibility = View.VISIBLE
                undoBtn.visibility = View.VISIBLE
                saveBtn.visibility = View.VISIBLE
                colourBtn.visibility = View.VISIBLE
                strokeBtn.visibility = View.VISIBLE
                eraserBtn.visibility = View.VISIBLE
                clearBtn.visibility = View.VISIBLE
                rangeSlider.visibility = View.VISIBLE
            }
        }
        saveBtn.setOnClickListener {
            val saveDialog = AlertDialog.Builder(requireContext())
            saveDialog.setTitle("Save Drawing")
            saveDialog.setMessage("Save drawing to your device gallery?")
            saveDialog.setPositiveButton("Yes") { dialog, which ->
                val bmp = paint.save()
                var imageOutStream: OutputStream? = null

                val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                val currentDateTime = dateFormat.format(Date())

                val fileName = "drawing_$currentDateTime.png"

                val cv = ContentValues()
                cv.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val uri: Uri? = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
                try {
                    imageOutStream = requireContext().contentResolver.openOutputStream(uri!!)
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream!!)
                    imageOutStream.close()
                    Toast.makeText(requireContext(), "Drawing saved to gallery!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            saveDialog.setNegativeButton("No") { dialog, which -> dialog.cancel() }
            saveDialog.show()
        }


        colourBtn.setOnClickListener {
            isBackground = false
            openDialog()

        }

        strokeBtn.setOnClickListener {
            paint.setEraser(false)
            paint.setColour(currentColour)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            rangeSlider.min = 0
            rangeSlider.max = 100
        }

        rangeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, i: Int, b: Boolean) {
                paint.setStrokeWidth(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val vto = paint.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                paint.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = paint.width
                val height = paint.height
                paint.setUpCanvas(height, width)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemID = item.itemId
        if (itemID == android.R.id.home) {
          //  finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDialog(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.colors_layout)
        val width = WindowManager.LayoutParams.MATCH_PARENT
        val height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.setLayout(width, height)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.show()
        dialog.findViewById<CardView>(R.id.blackColor).setOnClickListener {
            paint.setColour(Color.BLACK)
            dialog.dismiss()
        }
        dialog.findViewById<CardView>(R.id.whiteColor).setOnClickListener {
            paint.setColour(Color.WHITE)
            dialog.dismiss()
        }
        dialog.findViewById<CardView>(R.id.redColor).setOnClickListener {
            paint.setColour(Color.RED)
            dialog.dismiss()
        }

        dialog.findViewById<CardView>(R.id.greenColor).setOnClickListener {
            paint.setColour(Color.GREEN)
            dialog.dismiss()
        }
        dialog.findViewById<CardView>(R.id.yellowColor).setOnClickListener {
            paint.setColour(Color.YELLOW)
            dialog.dismiss()
        }
        dialog.findViewById<CardView>(R.id.blueColor).setOnClickListener {
            paint.setColour(Color.BLUE)
            dialog.dismiss()
        }
        dialog.findViewById<CardView>(R.id.cyanColor).setOnClickListener {
            paint.setColour(Color.CYAN)
            dialog.dismiss()
        }
    }

}
