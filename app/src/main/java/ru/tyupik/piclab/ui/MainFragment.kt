package ru.tyupik.piclab.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            pictureViewModel.loadNextPicture()
            Glide.with(binding.ivPicture)
                .asGif()
                .load(pictureViewModel.pictureLiveData.value?.get(pictureViewModel.num)?.gifURL)
                .fitCenter()
                .error(R.drawable.ic_plug_24)
                .timeout(10_000)
                .into(binding.ivPicture)
            binding.tvDescription.text = pictureViewModel.pictureLiveData.value?.get(pictureViewModel.num)?.description
        }

        pictureViewModel.pictureLiveData.observe(viewLifecycleOwner, {
            print (it.toString())
            Glide.with(binding.ivPicture)
                .asGif()
                .load(it.last().gifURL)
                .fitCenter()
                .error(R.drawable.ic_arrow_forward__64)
                .timeout(10_000)
                .into(binding.ivPicture)
            binding.tvDescription.text = it.last().description

            binding.btnPrev.setOnClickListener {view ->
                pictureViewModel.loadPreviousPicture()
                print (it[pictureViewModel.num])
                Glide.with(binding.ivPicture)
                    .asGif()
                    .load(it[pictureViewModel.num].gifURL)
                    .fitCenter()
                    .error(R.drawable.ic_plug_24)
                    .timeout(10_000)
                    .into(binding.ivPicture)
                binding.tvDescription.text = it[pictureViewModel.num].description
            }
        })

        pictureViewModel.dataState.observe(viewLifecycleOwner, { state ->
            if (state.firstPicture) {
                binding.btnPrev.isClickable = false
                binding.btnPrev.setIconTintResource(R.color.undo_un)
            } else {
                binding.btnPrev.isClickable = true
                binding.btnPrev.setIconTintResource(R.color.undo_av)
            }
            if (state.error) {
                Toast.makeText(requireContext(), getString(R.string.retry), Toast.LENGTH_SHORT).show()
            }
        })
        return binding.root
    }

}