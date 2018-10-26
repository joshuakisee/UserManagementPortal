package com.usermportal.usermanagementportal.util.adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.usermportal.usermanagementportal.R;
import com.usermportal.usermanagementportal.activites.MainActivity;
import com.usermportal.usermanagementportal.util.Model.Model;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder>{

    private List<Model> models;
    private Context mContext;
    String id;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout listlinear;
        CircularImageView avater;
        ImageButton mImageButton;
        TextView name;

        public MyViewHolder(View view) {
            super(view);

            name        = (TextView) view.findViewById(R.id.name);
            listlinear  = (LinearLayout) view.findViewById(R.id.listlinear);
            avater      = (CircularImageView) view.findViewById(R.id.icons);
            mImageButton= (ImageButton) view.findViewById(R.id.imageButton);

        }
    }


    public UserListAdapter(Context context, List<Model> models) {
        mContext = context;
        this.models = models;
    }

    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layoutuser, parent, false);

        return new UserListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserListAdapter.MyViewHolder holder, final int position) {

        final Model model = models.get(position);

        holder.name.setText(model.getFirst_name()+" "+model.getLast_name());

        Picasso.get().load(model.getAvatar())
                .placeholder(R.mipmap.icon)
                .error(R.mipmap.icon)
                .into(holder.avater);


        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.mImageButton,position);
            }
        });

        id = model.getId();


    }

    private void showPopupMenu(View view,int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_actions, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }


    @Override
    public int getItemCount() {
        return models.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private UserListAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final UserListAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        public MyMenuItemClickListener(int positon) {
            this.position=positon;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.action_delete:
                    MainActivity.getInstace().delete(id);
                    //Toast.makeText(mContext, ""+id, Toast.LENGTH_SHORT).show();
                    return true;

                default:
            }
            return false;
        }
    }

}
