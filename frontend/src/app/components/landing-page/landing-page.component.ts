// landing-page.component.ts
import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';

interface FeatureCard {
  icon: string;
  title: string;
  description: string;
}

interface StatItem {
  value: string;
  label: string;
  count: number;
}

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.scss'
})
export class LandingPageComponent {
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  // Signals for reactive state
  loading = signal(false);
  shortLink = signal('');

  // Form setup
  form: FormGroup = this.fb.group({
    targetUrl: ['', [
      Validators.required,
      Validators.pattern(/^https?:\/\/[^\s]+$/)
    ]]
  });

  // Features data
  features = signal<FeatureCard[]>([
    {
      icon: '📊',
      title: 'Analyses en temps réel',
      description: 'Surveillez les performances de vos liens avec des analyses détaillées incluant le suivi des clics, les données géographiques et les informations de référence.'
    },
    {
      icon: '🏠',
      title: 'Auto-hébergé',
      description: 'Contrôle total de vos données avec un déploiement auto-hébergé. Déployez facilement avec Docker ou Kubernetes pour une flexibilité maximale.'
    },
    {
      icon: '⚡',
      title: 'Architecture événementielle',
      description: 'Construit avec des microservices et des modèles événementiels pour la scalabilité, la fiabilité et une séparation claire des préoccupations.'
    },
    {
      icon: '🎨',
      title: 'Interface moderne',
      description: 'Interface web propre et intuitive conçue pour la facilité d\'utilisation tout en fournissant des capacités de gestion de liens puissantes.'
    },
    {
      icon: '🔧',
      title: 'Prêt pour la production',
      description: 'Démontre une conception backend propre avec une gestion d\'erreurs appropriée, une journalisation et une surveillance pour les environnements de production.'
    },
    {
      icon: '🚀',
      title: 'Liens courts personnalisés',
      description: 'Créez des liens courts de marque avec des domaines personnalisés et des alias pour maintenir l\'identité de votre marque.'
    }
  ]);

  // Stats data
  stats = signal<StatItem[]>([
    { value: '1M+', label: 'Liens raccourcis', count: 1000000 },
    { value: '5M+', label: 'Clics suivis', count: 5000000 },
    { value: '10K+', label: 'Utilisateurs satisfaits', count: 10000 },
    { value: '99.9%', label: 'Disponibilité', count: 99.9 }
  ]);

  imagePath: String = 'assets/rikikilogo3.svg';
  title: String = 'RikikiLink';

  // Form submission handler
  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.shortLink.set('');

    // Simulate API call - Replace with your actual service
    setTimeout(() => {
      const randomId = Math.random().toString(36).substring(2, 8);
      const mockShortUrl = `https://rkk.link/${randomId}`;

      this.shortLink.set(mockShortUrl);
      this.loading.set(false);

      this.snackBar.open('Lien raccourci créé avec succès!', 'Fermer', {
        duration: 3000,
        horizontalPosition: 'center',
        verticalPosition: 'top'
      });
    }, 1500);
  }

  copy(): void {
    const shortUrl = this.shortLink();
    if (shortUrl) {
      navigator.clipboard.writeText(shortUrl).then(() => {
        this.snackBar.open('Lien copié dans le presse-papiers!', 'Fermer', {
          duration: 2000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }).catch(() => {
        // Fallback for older browsers
        const textArea = document.createElement('textarea');
        textArea.value = shortUrl;
        document.body.appendChild(textArea);
        textArea.select();
        document.execCommand('copy');
        document.body.removeChild(textArea);

        this.snackBar.open('Lien copié!', 'Fermer', {
          duration: 2000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      });
    }
  }
}