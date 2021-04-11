package com.newsolutions.newsyapp.ui.sumoperation;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.newsolutions.newsyapp.R;
import com.newsolutions.newsyapp.data.roomcontentprovider.SumOp;
import com.newsolutions.newsyapp.databinding.CalculatorFragmentBinding;
import java.util.Objects;

public class CalculatorFragment extends Fragment {

    private CalculatorViewModel mViewModel;
    private CalculatorFragmentBinding binding;
    private View v;
    private static final String TAG = "CalculatorFragment";

    public static CalculatorFragment newInstance() {
        return new CalculatorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.calculator_fragment, container, false);
        v = binding.getRoot();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        validateForm();
    }

    private void validateForm() {
        mViewModel.getNum().observe(getViewLifecycleOwner(), new Observer<SumOp>() {
            @Override
            public void onChanged(@Nullable SumOp num) {
                Log.e(TAG, "onChanged: nums " + num.getNum1() + num.getNum2());
                if (TextUtils.isEmpty(Objects.requireNonNull(num).getNum1())) {
                    binding.etNum1.setError("Enter num 1");
                    binding.etNum1.requestFocus();
                } else if (TextUtils.isEmpty(Objects.requireNonNull(num).getNum2())) {
                    binding.etNum2.setError("Enter num 1");
                    binding.etNum2.requestFocus();
                } else {
                      mViewModel.calc(num,v);
                    Toast.makeText(requireContext(),Integer.parseInt(num.getNum1())+Integer.parseInt(num.getNum2())+"",Toast.LENGTH_LONG).show();
                   //   mViewModel.getData(v);
                }
            }
        });
    }
}