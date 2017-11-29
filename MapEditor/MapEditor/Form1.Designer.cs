namespace MapEditor
{
    partial class Form1
    {
        /// <summary>
        /// Wymagana zmienna projektanta.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Wyczyść wszystkie używane zasoby.
        /// </summary>
        /// <param name="disposing">prawda, jeżeli zarządzane zasoby powinny zostać zlikwidowane; Fałsz w przeciwnym wypadku.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Kod generowany przez Projektanta formularzy systemu Windows

        /// <summary>
        /// Wymagana metoda obsługi projektanta — nie należy modyfikować 
        /// zawartość tej metody z edytorem kodu.
        /// </summary>
        private void InitializeComponent()
        {
            this.pictureBox1 = new System.Windows.Forms.PictureBox();
            this.wysokosc = new System.Windows.Forms.NumericUpDown();
            this.szerokosc = new System.Windows.Forms.NumericUpDown();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.zapisz_btn = new System.Windows.Forms.Button();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.podglad = new System.Windows.Forms.PictureBox();
            this.checkBox1 = new System.Windows.Forms.CheckBox();
            this.comboBox1 = new System.Windows.Forms.ComboBox();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.wysokosc)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.szerokosc)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.podglad)).BeginInit();
            this.SuspendLayout();
            // 
            // pictureBox1
            // 
            this.pictureBox1.Location = new System.Drawing.Point(139, 12);
            this.pictureBox1.Name = "pictureBox1";
            this.pictureBox1.Size = new System.Drawing.Size(640, 640);
            this.pictureBox1.SizeMode = System.Windows.Forms.PictureBoxSizeMode.AutoSize;
            this.pictureBox1.TabIndex = 0;
            this.pictureBox1.TabStop = false;
            this.pictureBox1.MouseClick += new System.Windows.Forms.MouseEventHandler(this.pictureBox1_MouseClick);
            // 
            // wysokosc
            // 
            this.wysokosc.Location = new System.Drawing.Point(12, 26);
            this.wysokosc.Name = "wysokosc";
            this.wysokosc.Size = new System.Drawing.Size(120, 20);
            this.wysokosc.TabIndex = 1;
            this.wysokosc.ValueChanged += new System.EventHandler(this.wysokosc_ValueChanged);
            // 
            // szerokosc
            // 
            this.szerokosc.Location = new System.Drawing.Point(12, 66);
            this.szerokosc.Name = "szerokosc";
            this.szerokosc.Size = new System.Drawing.Size(120, 20);
            this.szerokosc.TabIndex = 2;
            this.szerokosc.ValueChanged += new System.EventHandler(this.szerokosc_ValueChanged);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(9, 9);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(57, 13);
            this.label1.TabIndex = 3;
            this.label1.Text = "Wysokość";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(9, 50);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(57, 13);
            this.label2.TabIndex = 4;
            this.label2.Text = "Szerokość";
            // 
            // zapisz_btn
            // 
            this.zapisz_btn.Location = new System.Drawing.Point(11, 236);
            this.zapisz_btn.Name = "zapisz_btn";
            this.zapisz_btn.Size = new System.Drawing.Size(75, 23);
            this.zapisz_btn.TabIndex = 6;
            this.zapisz_btn.Text = "Zapisz";
            this.zapisz_btn.UseVisualStyleBackColor = true;
            this.zapisz_btn.Click += new System.EventHandler(this.zapisz_btn_Click);
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(11, 210);
            this.textBox1.Name = "textBox1";
            this.textBox1.Size = new System.Drawing.Size(102, 20);
            this.textBox1.TabIndex = 7;
            // 
            // podglad
            // 
            this.podglad.Location = new System.Drawing.Point(35, 121);
            this.podglad.Name = "podglad";
            this.podglad.Size = new System.Drawing.Size(64, 64);
            this.podglad.TabIndex = 9;
            this.podglad.TabStop = false;
            // 
            // checkBox1
            // 
            this.checkBox1.AutoSize = true;
            this.checkBox1.Location = new System.Drawing.Point(13, 191);
            this.checkBox1.Name = "checkBox1";
            this.checkBox1.Size = new System.Drawing.Size(74, 17);
            this.checkBox1.TabIndex = 10;
            this.checkBox1.Text = "Przejście?";
            this.checkBox1.UseVisualStyleBackColor = true;
            // 
            // comboBox1
            // 
            this.comboBox1.FormattingEnabled = true;
            this.comboBox1.Location = new System.Drawing.Point(12, 265);
            this.comboBox1.Name = "comboBox1";
            this.comboBox1.Size = new System.Drawing.Size(123, 21);
            this.comboBox1.TabIndex = 11;
            this.comboBox1.SelectedIndexChanged += new System.EventHandler(this.comboBox1_SelectedIndexChanged);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoScroll = true;
            this.ClientSize = new System.Drawing.Size(812, 673);
            this.Controls.Add(this.comboBox1);
            this.Controls.Add(this.checkBox1);
            this.Controls.Add(this.podglad);
            this.Controls.Add(this.textBox1);
            this.Controls.Add(this.zapisz_btn);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.szerokosc);
            this.Controls.Add(this.wysokosc);
            this.Controls.Add(this.pictureBox1);
            this.Name = "Form1";
            this.Text = "Form1";
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.wysokosc)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.szerokosc)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.podglad)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.PictureBox pictureBox1;
        private System.Windows.Forms.NumericUpDown wysokosc;
        private System.Windows.Forms.NumericUpDown szerokosc;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button zapisz_btn;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.PictureBox podglad;
        private System.Windows.Forms.CheckBox checkBox1;
        private System.Windows.Forms.ComboBox comboBox1;
    }
}

