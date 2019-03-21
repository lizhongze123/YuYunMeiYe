package cn.yuyun.yymy.ui.home.member;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.ui.home.member.memberdata.MemberDetailActivity;
import cn.yuyun.yymy.ui.me.entity.MemberBean;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/1/20
 */

public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_PERSON = 1;

    public ExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_level);
        addItemType(TYPE_PERSON, R.layout.item_member);
    }

    @Override
    protected void convert(final BaseViewHolder holder, MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final Level0Item lv0 = (Level0Item) item;
                holder.setText(R.id.tv_title, lv0.title+"")
                        .setImageResource(R.id.iv_expan, lv0.isExpanded() ? R.drawable.item_open : R.drawable.item_close);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }});
                break;
            case TYPE_PERSON:
                final MemberBean person = (MemberBean) item;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getAdapterPosition();
                        remove(pos);
                        view.getContext().startActivity(new Intent(view.getContext(),MemberDetailActivity.class));
                    }
                });
                break;
                default:
        }
    }

}
