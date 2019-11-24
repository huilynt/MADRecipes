package com.madrecipes.app.madrecipes.EditScreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.madrecipes.app.madrecipes.R;
import com.madrecipes.app.madrecipes.Data.DBHelper;
import com.madrecipes.app.madrecipes.Data.Recipe;
import com.madrecipes.app.madrecipes.Data.Steps;
import com.madrecipes.app.madrecipes.TouchHelper.OnDragStartListener;
import com.madrecipes.app.madrecipes.TouchHelper.SimpleItemTouchHelperCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//Tay Hui Lin
//Team 6
//10178003J

//edit existing recipe
public class EditActivity extends AppCompatActivity implements OnDragStartListener {
    DBHelper dbHelper;
    EditAdapter editAdapter;
    RecyclerView recyclerView;
    Button btnUpdate, btnAddStep;
    ImageView imageView;
    TextInputLayout recipeName, recipeStep;
    ItemTouchHelper mItemTouchHelper;
    List<Steps> stepsList = new ArrayList<>();
    List<String> stepList = new ArrayList<>();
    final int GALLERY = 1;
    final int CAMERA = 2;
    String id;
    Recipe recipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        getSteps();
    }

    //initialize
    private void init() {
        recyclerView = findViewById(R.id.lvRecyclerView);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnAddStep = findViewById(R.id.btnAddStep);
        imageView = findViewById(R.id.imageChosen);
        recipeName = findViewById(R.id.etRecipeName);
        recipeStep = findViewById(R.id.etRecipeStep);

        id = getIntent().getExtras().getString("recipeID");
        dbHelper = new DBHelper(this);
        recipe = dbHelper.getRecipeByID(id);

        setData();

        //choose recipe image by clicking on image view
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) { //permission is allowed
                    showPictureDialog();
                } else if (!checkPermission()){ //permission is revoked by user
                    new AlertDialog.Builder(v.getContext())
                            .setMessage("Please allow access to camera and storage to add recipe image.")
                            .setNeutralButton("Back",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                }

            }
        });

        //update recipe
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateName() & validateStepList()) { //name and steplist not empty
                    updateRecipe();
                    Toast("Recipe updated!");
                    finish();
                } else {
                    return;
                }
            }
        });

        //add step to steplist
        btnAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateStep()) { //step is not empty
                    addStep();
                } else {
                    return;
                }
            }
        });

        editAdapter = new EditAdapter(this, stepList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(editAdapter);

        //for swiping and switching position
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(editAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    //update existing recipe
    private void updateRecipe() {
        String name = recipeName.getEditText().getText().toString();
        byte[] image = imageViewToByte(imageView);

        Recipe recipe = new Recipe();
        recipe.setId(Integer.parseInt(id));
        recipe.setRecipeName(name);
        recipe.setRecipeImage(image);

        dbHelper.updateRecipe(recipe, stepList);
    }

    //check if name edittext is empty
    //if yes, set error & return false
    //if no, return true
    private boolean validateName() {
        String name = recipeName.getEditText().getText().toString().trim();
        if (name.isEmpty()) {
            recipeName.setError("Enter a name");
            return false;
        } else {
            recipeName.setError(null);
            return true;
        }
    }

    //check if steplist is empty
    //if yes, set error & return false
    //if no, return true
    private boolean validateStepList() {
        if (stepList.isEmpty()) {
            recipeStep.setError("Enter at least one step");
            return false;
        } else {
            return true;
        }
    }

    //check if step edittext is empty
    //if yes, set error & return false
    //if no, return true
    private boolean validateStep() {
        String step = recipeStep.getEditText().getText().toString().trim();
        if (step.isEmpty()) {
            recipeStep.setError("Enter a step");
            return false;
        } else {
            recipeStep.setError(null);
            return true;
        }
    }

    //set the image, recipe name, and steps
    //to the ones for the chosen recipe
    private void setData() {
        byte[] image = recipe.getRecipeImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        imageView.setImageBitmap(bitmap);

        String name = recipe.getRecipeName();
        recipeName.getEditText().setText(name);
    }

    //get all steps of recipe to be edited
    private void getSteps() {
        if (dbHelper.getAllSteps(id) != null && dbHelper.getAllRecipe() != null ) {
            stepsList.addAll(dbHelper.getAllSteps(id));
            for (int i = 0; i < stepsList.size(); i++) {
                Steps step = stepsList.get(i);
                stepList.add(step.getStep());
            }
        } else {
            return;
        }
    }

    //add step to steplist
    private void addStep() {
        stepList.add(recipeStep.getEditText().getText().toString());
        editAdapter.notifyDataSetChanged();
        recipeStep.getEditText().getText().clear();
    }

    //change imageview bitmap to byte
    //to store in database
    private byte[] imageViewToByte(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (drawable instanceof VectorDrawableCompat || drawable instanceof VectorDrawable) {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            }
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] bytes = stream.toByteArray();
        return bytes;
    }

    //check if app has permission to access camera and phone storage
    private boolean checkPermission() {
        int result;
        String[] permissions = new String[]{
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    //dialog to prompt user to choose method of getting image
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select from gallery",
                "Capture from camera"
        };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                chooseFromGallery();
                                break;
                            case 1:
                                takeFromCamera();
                        }
                    }
                });
        pictureDialog.show();
    }

    //open gallery
    private void chooseFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
    }

    //open camera
    private void takeFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    //after selecting image in gallery or camera
    //set imageview to selected image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = null;
            if (requestCode == GALLERY) {
                Uri uri = data.getData();

                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);

                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == CAMERA) {
                bitmap = (Bitmap) data.getExtras().get("data");
            }
            imageView.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //display a toast
    private void Toast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    //tells when user made a drag action
    @Override
    public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
