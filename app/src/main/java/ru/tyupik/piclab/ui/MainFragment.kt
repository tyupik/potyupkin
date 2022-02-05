package ru.tyupik.piclab.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.tyupik.piclab.R
import ru.tyupik.piclab.databinding.FragmentMainBinding
import ru.tyupik.piclab.viewmodel.PictureViewModel

class MainFragment : Fragment() {

    private val pictureViewModel = PictureViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(
            inflater,
            container,
            false
        )
        pictureViewModel.loadPicture()

        binding.btnNext.setOnClickListener {
            pictureViewModel.loadPicture()
            print(pictureViewModel.data.last())
            val a = pictureViewModel.data.last().gifURL
            print(a)
            Glide.with(binding.ivPicture)
                .asGif()
                .load(pictureViewModel.data.last().gifURL)
                .error(R.drawable.ic_plug_24)
                .timeout(10_000)
                .into(binding.ivPicture)
            binding.tvDescription.text = pictureViewModel.data.last().description
        }
        return binding.root
    }

}