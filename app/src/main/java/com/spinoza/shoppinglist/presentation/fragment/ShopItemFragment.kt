package com.spinoza.shoppinglist.presentation.fragment

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.spinoza.shoppinglist.databinding.FragmentShopItemBinding
import com.spinoza.shoppinglist.domain.model.ShopItem
import com.spinoza.shoppinglist.presentation.ShopListApp
import com.spinoza.shoppinglist.presentation.viewmodel.ShopItemViewModel
import com.spinoza.shoppinglist.presentation.viewmodel.ViewModelFactory
import javax.inject.Inject

class ShopItemFragment : Fragment() {
    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID
    private var savedName: String? = null
    private var savedCount: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as ShopListApp).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)

        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else throw RuntimeException("Activity must implement OnEditingFinishedListener")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            savedName = it.getString(SAVED_NAME)
            savedCount = it.getString(SAVED_COUNT)
        }
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupScreen()
        launchRightMode()
    }

    override fun onResume() {
        super.onResume()
        savedName?.let { binding.editTextName.setText(it) }
        savedCount?.let { binding.editTextCount.setText(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SAVED_NAME, binding.editTextName.text?.toString())
        outState.putString(SAVED_COUNT, binding.editTextCount.text?.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchRightMode() {
        if (screenMode == MODE_EDIT) {
            viewModel.getShopItem(shopItemId)
        }
    }

    private fun setupScreen() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }

        binding.buttonSave.setOnClickListener {
            viewModel.saveShopItem(
                binding.editTextName.text?.toString(),
                binding.editTextCount.text?.toString()
            )

            if (screenMode == MODE_ADD) {
                context?.contentResolver?.insert(
                    Uri.parse("content://com.spinoza.shoppinglist/shop_items/35"),
                    ContentValues().apply {
                        put("id", 0)
                        put("name", binding.editTextName.text.toString())
                        put("count", binding.editTextCount.text.toString().toFloat())
                        put("enabled", true)
                    }
                )
            }
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (args.containsKey(SCREEN_MODE)) {
            val mode = args.getString(SCREEN_MODE)
            if (mode == MODE_ADD || mode == MODE_EDIT) {
                screenMode = mode
                if (screenMode == MODE_EDIT) {
                    if (args.containsKey(SHOP_ITEM_ID)) {
                        shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
                    } else throw RuntimeException("Param shop item id is absent")
                }
            } else throw RuntimeException("Unknown screen mode $mode")
        } else throw RuntimeException("Param screen mode is absent")
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    companion object {
        private const val SAVED_NAME = "name"
        private const val SAVED_COUNT = "count"
        const val SHOP_ITEM_ID = "extra_shop_item_id"
        const val SCREEN_MODE = "extra_mode"
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
        const val MODE_UNKNOWN = "mode_unknown"

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}