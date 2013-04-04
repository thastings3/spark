package com.gatech.spark.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;
import com.gatech.spark.R;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/3/13
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class GenericArrayAdapter <T> extends ArrayAdapter<T> implements Filterable
{
    private ArrayList<T> mData;
    private Context context;

    public GenericArrayAdapter( Context context, ArrayList<T> mData )
    {
        super( context, R.layout.list_item );
        this.context = context;
        this.mData = mData;
    }

    static class ViewHolder
    {
        protected TextView text;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        ViewHolder viewHolder;
        if ( convertView == null )
        {
            convertView = View.inflate( context, R.layout.list_item, null );
            viewHolder = new ViewHolder();
            viewHolder.text = ( TextView ) convertView.findViewById( R.id.text1 );
            convertView.setTag( viewHolder );
        }
        else
        {
            viewHolder = ( ViewHolder ) convertView.getTag();
        }

        viewHolder.text.setText( mData.get( position ).toString() );

        return convertView;
    }

    @Override
    public int getCount()
    {
        return mData.size();
    }

    @Override
    public T getItem( int index )
    {
        return mData.get( index );
    }
}
