package com.direct2web.citysip.Adapter.RestaurentAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.direct2web.citysip.Activities.CommonActivities.IntroLoginActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorAboutYouActivity;
import com.direct2web.citysip.Activities.Doctor.DoctorDeshboardActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerAboutYouActivity;
import com.direct2web.citysip.Activities.Lawyer.LawyerDashboardActivity;
import com.direct2web.citysip.Activities.Restaurent.AddRestaurantDetailsActivity;
import com.direct2web.citysip.Activities.Restaurent.MainActivity;
import com.direct2web.citysip.Model.RestaurentModels.Category.Category;
import com.direct2web.citysip.R;
import com.direct2web.citysip.Utils.Api;
import com.direct2web.citysip.Utils.SessionManager;
import com.direct2web.citysip.databinding.RawCategoryListBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    List<Category> categoryList;
    int flag = 0;
    SessionManager sessionManager;
    onClickCategory onClickCategory;

    public CategoryAdapter(Context context, List<Category> categoryList,int flag,SessionManager sessionManager) {
        this.context = context;
        this.categoryList = categoryList;
        this.flag = flag;
        this.sessionManager=sessionManager;
    }

    public CategoryAdapter(Context context, List<Category> categoryList,onClickCategory onClickCategory) {
        this.context = context;
        this.categoryList = categoryList;
       this.onClickCategory=onClickCategory;
    }



    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RawCategoryListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.raw_category_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Category cl = categoryList.get(position);

        Glide.with(context).load(Api.imageUrl+cl.getImage())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.categoryImage);

        holder.binding.txtid.setText(cl.getId());
        holder.binding.txtName.setText(cl.getName());

        holder.binding.rlCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickCategory.onCategoryItemClick(position);
               /* switch(cl.getId()) {
                    case "1":
                        if (sessionManager.checkLogin()) {
                            if (sessionManager.checkLawyer()) {
                                Intent rest_intent = new Intent(context, LawyerDashboardActivity.class);
                                context.startActivity(rest_intent);
                                sessionManager.setBusinesstype("1");
                            } else {
                                Intent rest_intent = new Intent(context, LawyerAboutYouActivity.class);
                                context.startActivity(rest_intent);
                                sessionManager.setBusinesstype("1");
                            }
                        } else {
                            Intent rest_intent = new Intent(context, IntroLoginActivity.class);
                            context.startActivity(rest_intent);
                            sessionManager.setBusinesstype("1");
                        }

                        break;

                    case "4":
                        if (sessionManager.checkLogin()) {
                            if (sessionManager.checkDoctor()) {
                                Intent doc_intent = new Intent(context, DoctorDeshboardActivity.class);
                                context.startActivity(doc_intent);
                                sessionManager.setBusinesstype("4");
                            } else {
                                Intent doc_intent = new Intent(context, DoctorAboutYouActivity.class);
                                context.startActivity(doc_intent);
                                sessionManager.setBusinesstype("4");
                            }
                        } else {

                            Intent doc_intent = new Intent(context, IntroLoginActivity.class);
                            context.startActivity(doc_intent);
                            sessionManager.setBusinesstype("4");
                        }
                        break;
                    case "6":
                        if (sessionManager.checkLogin()) {
                            if (sessionManager.checkRestaurant()) {
                                Intent rest_intent = new Intent(context, MainActivity.class);
                                context.startActivity(rest_intent);
                                sessionManager.setBusinesstype("6");
                            } else {
                                Intent rest_intent = new Intent(context, AddRestaurantDetailsActivity.class);
                                context.startActivity(rest_intent);
                                sessionManager.setBusinesstype("6");
                            }
                        } else {
                            Intent rest_intent = new Intent(context, IntroLoginActivity.class);
                            context.startActivity(rest_intent);
                            sessionManager.setBusinesstype("6");
                        }
                        break;

                    default:
                        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                        break;
                }*/

            }
        });





    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        RawCategoryListBinding binding;

        public CategoryViewHolder(@NonNull RawCategoryListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface onClickCategory {
        public void onCategoryItemClick(int position);
    }
}
