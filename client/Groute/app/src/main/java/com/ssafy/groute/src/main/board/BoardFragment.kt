package com.ssafy.groute.src.main.board

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.databinding.FragmentBoardBinding
import com.ssafy.groute.src.main.MainActivity


class BoardFragment : Fragment() {
    lateinit var binding: FragmentBoardBinding
    private lateinit var mainActivity: MainActivity
    lateinit var boardRecyclerviewAdapter: BoardRecyclerviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        boardRecyclerviewAdapter = BoardRecyclerviewAdapter()

        binding.boardRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = boardRecyclerviewAdapter

        }
    }
}